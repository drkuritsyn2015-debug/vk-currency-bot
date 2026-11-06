
package com.example.vk_currency_bot.service.ai;

import com.example.vk_currency_bot.config.AiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ai.provider", havingValue = "ollama", matchIfMissing = true)
public class OllamaAiClient implements AiClient {

    private final ObjectMapper objectMapper;
    private final AiProperties aiProperties;

    @Qualifier("aiRestClient")
    private final RestClient aiRestClient;

    @Override
    public String chatAi(String prompt) { // <--- ПРИНИМАЕМ ГОТОВЫЙ ПРОМПТ
        try {
            Map<String, Object> body = Map.of(
                    "model", aiProperties.getModel(),
                    "prompt", prompt,
                    "stream", false
            );
            log.info("Отправка запроса к Ollama...");

            String response = aiRestClient.post()
                    .uri(aiProperties.getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            return root.path("response").asText();

        } catch (Exception e) {
            log.error("Ошибка при запросе к Ollama", e);
            return null;
        }
    }
}