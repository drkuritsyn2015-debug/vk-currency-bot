package com.example.vk_currency_bot.handler;

import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.service.AiService;
import com.example.vk_currency_bot.service.CurrencyAliases;
import com.example.vk_currency_bot.service.UserService;
import com.example.vk_currency_bot.service.VkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class ReadyHandler implements MessageHandler {

    private final VkService vkService;
    private final UserService userService;
    private final AiService aiService;
    private final CurrencyAliases currencyAliases;
    private final CurrencyHandler currencyHandler;
    private final Executor aiTaskExecutor;

    @Override
    public DialogState getState() {
        return DialogState.READY;
    }

    @Override
    public void handle(VkUser user, String text) {
        String lowerText = text.toLowerCase().trim();

        if (lowerText.contains("привет") || lowerText.contains("здравствуй")) {
            vkService.sendMessage(user.getVkUserId(), "Привет, " + user.getFirstName() + "! Какую валюту посмотреть?");
            userService.updateState(user, DialogState.WAITING_CURRENCY);
            return;
        }

        if (currencyAliases.findCode(lowerText) != null) {
            currencyHandler.handle(user, text);
            return;
        }

        CompletableFuture.runAsync(() -> {
            String aiResponse = aiService.chat(text);
            String finalText = aiResponse != null ? aiResponse : "Напиши название валюты (например, 'доллар'), и я дам курс.";
            vkService.sendMessage(user.getVkUserId(), finalText);
        }, aiTaskExecutor);
    }
}