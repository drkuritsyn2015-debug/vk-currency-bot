package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.config.VkProperties;
import com.example.vk_currency_bot.dto.vk.VkLongPollServerResponse;
import com.example.vk_currency_bot.dto.vk.VkResponseWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class VkLongPollService implements ApplicationRunner {

    private final VkProperties vkProperties;
    private final DialogService dialogService;
    private final VkService vkService;
    private final ObjectMapper objectMapper;
    private final Executor taskExecutor;
    private final RestClient fastRestClient;

    private String server;
    private String key;
    private String ts;

    public VkLongPollService(
            VkProperties vkProperties,
            DialogService dialogService,
            VkService vkService,
            ObjectMapper objectMapper,
            Executor taskExecutor,
            @Qualifier("fastRestClient") RestClient fastRestClient) {
        this.vkProperties = vkProperties;
        this.dialogService = dialogService;
        this.vkService = vkService;
        this.objectMapper = objectMapper;
        this.taskExecutor = taskExecutor;
        this.fastRestClient = fastRestClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        CompletableFuture.runAsync(this::startListening, taskExecutor);
    }

    public void startListening() {
        log.info("Запуск Long Poll...");
        getLongPollServer();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                String url = "https://" + server + "?act=a_check&key=" + key + "&ts=" + ts + "&wait=" + vkProperties.getLongpollWait();
                String response = fastRestClient.get().uri(url).retrieve().body(String.class);
                JsonNode root = objectMapper.readTree(response);

                if (root.has("failed")) {
                    getLongPollServer();
                    continue;
                }

                ts = root.get("ts").asText();
                JsonNode updates = root.get("updates");

                if (updates != null && updates.size() > 0) {
                    for (JsonNode update : updates) {
                        if (update.isArray() && update.size() > 0 && update.get(0).asInt() == 4) {
                            int flags = update.get(2).asInt();
                            if ((flags & 2) != 0) continue;

                            long fromId = update.get(3).asLong();
                            String text = update.get(6).asText();

                            log.info("Пришло сообщение от {}: {}", fromId, text);

                            try {
                                CompletableFuture.runAsync(() -> {
                                    dialogService.processIncomingMessage(fromId, text);
                                }, taskExecutor).exceptionally(ex -> {
                                    log.error("Ошибка в CompletableFuture", ex);
                                    return null;
                                });
                            } catch (java.util.concurrent.RejectedExecutionException e) {
                                log.warn("Пул переполнен! Сообщение не обработано.");
                                vkService.sendMessage(fromId, "Слишком много запросов. Попробуйте позже.");
                            }
                        }
                    }
                }

            } catch (ResourceAccessException e) {
                log.debug("Long Poll таймаут (это нормально), переподключаемся...");
            } catch (Exception e) {
                log.error("Ошибка в цикле Long Poll", e);
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
                getLongPollServer();
            }
        }
    }

    private void getLongPollServer() {
        String url = vkProperties.getApiUrl() + "/messages.getLongPollServer?group_id=" + vkProperties.getGroupId()
                + "&access_token=" + vkProperties.getAccessToken() + "&v=" + vkProperties.getApiVersion();
        try {
            String response = fastRestClient.get().uri(url).retrieve().body(String.class);
            VkResponseWrapper<VkLongPollServerResponse> wrapper = objectMapper.readValue(response,
                    objectMapper.getTypeFactory().constructParametricType(VkResponseWrapper.class, VkLongPollServerResponse.class));

            if (wrapper.getResponse() != null) {
                this.server = wrapper.getResponse().getServer();
                this.key = wrapper.getResponse().getKey();
                this.ts = wrapper.getResponse().getTs();
                log.info("Long Poll сервер получен. ts={}", ts);
            }
        } catch (Exception e) {
            log.error("Не удалось получить Long Poll сервер", e);
        }
    }
}