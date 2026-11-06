package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.config.AiProperties;
import com.example.vk_currency_bot.service.ai.AiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {
    private final AiClient aiClient;
    private final AiProperties aiProperties;

    public AiService(AiClient aiClient, AiProperties aiProperties) {
        this.aiClient = aiClient;
        this.aiProperties = aiProperties;
    }

    public String chat(String userMessage) {
        String prompt = String.format("%s Сообщение пользователя: '%s'", aiProperties.getPrompt(), userMessage);
        String aiResponse = aiClient.chatAi(prompt);
        if (aiResponse == null) return null;
        if (aiResponse.matches(".*[\\u4E00-\\u9FFF].*")) {
            log.warn("ИИ вернул иероглифы. Сработала защита.");
            return null;
        }
        return aiResponse + aiProperties.getSuffix();
    }
}
