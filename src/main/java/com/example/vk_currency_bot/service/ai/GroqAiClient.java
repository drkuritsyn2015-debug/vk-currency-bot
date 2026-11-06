package com.example.vk_currency_bot.service.ai;

import com.example.vk_currency_bot.config.AiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(name = "ai.provider", havingValue = "groq")
public class GroqAiClient implements AiClient {
    private final ObjectMapper objectMapper;
    private final AiProperties aiProperties;
    private final RestClient aiRestClient;

    public GroqAiClient(ObjectMapper objectMapper, AiProperties aiProperties,
                        @Qualifier("aiRestClient") RestClient aiRestClient) {
        this.objectMapper = objectMapper;
        this.aiProperties = aiProperties;
        this.aiRestClient = aiRestClient;
    }

    @Override
    public String chatAi(String prompt) {
        try {
            // Формат OpenAI (Groq)
            Map<String, Object> body = Map.of(
                    "model", aiProperties.getModel(),
                    "messages", List.of(Map.of("role", "user", "content", prompt)),
                    "stream", false
            );
            log.info("Отправка запроса к Groq API...");
            String response = aiRestClient.post().uri(aiProperties.getUrl())
                    .header("Authorization", "Bearer " + aiProperties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(String.class);

            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error("Ошибка при запросе к Groq", e);
            return null;
        }
    }
}