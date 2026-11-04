package com.example.vk_currency_bot.service;
import com.example.vk_currency_bot.config.VkProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class VkService {
    private final VkProperties vkProperties;
    private final ObjectMapper objectMapper;
    @Qualifier("fastRestClient") private final RestClient fastRestClient;

    @Retryable(retryFor = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void sendMessage(long peerId, String text) {
        String url = vkProperties.getApiUrl() + "/messages.send";
        try {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("access_token", vkProperties.getAccessToken());
            body.add("v", vkProperties.getApiVersion());
            body.add("peer_id", String.valueOf(peerId));
            body.add("random_id", String.valueOf(System.nanoTime()));
            body.add("message", text);
            body.add("charset", "utf-8");

            String response = fastRestClient.post().uri(url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body)
                    .retrieve().body(String.class);
            log.info("Сообщение отправлено в peerId={}. Ответ: {}", peerId, response);
        } catch (ResourceAccessException e) {
            log.warn("Сетевая ошибка при отправке в ВК..."); throw e;
        } catch (Exception e) {
            log.error("Ошибка ВК API...", e);
        }
    }

    @Recover
    public void recoverVk(ResourceAccessException e, long peerId, String text) {
        log.error("Не удалось отправить сообщение после 3 попыток. peerId={}", peerId);
    }

    public String getUserName(long userId) {
        String url = vkProperties.getApiUrl() + "/users.get?user_ids=" + userId
                + "&access_token=" + vkProperties.getAccessToken() + "&v=" + vkProperties.getApiVersion();
        try {
            String response = fastRestClient.get().uri(url).retrieve().body(String.class);
            JsonNode userNode = objectMapper.readTree(response).path("response").get(0);
            return userNode.path("first_name").asText() + " " + userNode.path("last_name").asText();
        } catch (Exception e) {
            log.error("Не удалось получить имя для ID {}", userId, e);
            return "друг";
        }
    }
}
