
package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.config.AiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final ObjectMapper objectMapper;
    private final AiProperties aiProperties;
    @Qualifier("aiRestClient")
    private final RestClient aiRestClient;

    //проверка для слабой Ollama.
    private static final Pattern CJK_PATTERN = Pattern.compile("[\\u4E00-\\u9FFF]");

    public String chat(String userMessage) {
        String prompt = String.format("%s Сообщение пользователя: '%s'", aiProperties.getPrompt(), userMessage);
        String aiResponse = callAi(prompt);


        if (aiResponse == null) {
            return null;
        }

    //проверка для слабой Ollama.
        if (CJK_PATTERN.matcher(aiResponse).find()) {
            log.warn("ИИ вернул иероглифы. Сработала защита. Ответ: {}", aiResponse);

            return null;
        }


        return aiResponse + aiProperties.getSuffix();
    }

    private String callAi(String prompt) {
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