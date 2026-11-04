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
public class MoreHandler implements MessageHandler {

    private final VkService vkService;
    private final UserService userService;
    private final AiService aiService;
    private final CurrencyAliases currencyAliases;
    private final CurrencyHandler currencyHandler;
    private final Executor aiTaskExecutor;

    @Override
    public DialogState getState() {
        return DialogState.WAITING_MORE;
    }

    @Override
    public void handle(VkUser user, String text) {
        String lowerText = text.toLowerCase().trim();

        if (lowerText.contains("нет") || lowerText.contains("пока") || lowerText.contains("хватит")) {
            vkService.sendMessage(user.getVkUserId(), "Хорошего дня, " + user.getFirstName() + "!");
            userService.updateState(user, DialogState.READY);
            return;
        }

        if (lowerText.equals("да") || lowerText.contains("ещё") || lowerText.contains("еще")) {
            vkService.sendMessage(user.getVkUserId(), "Какая валюта?");
            userService.updateState(user, DialogState.WAITING_CURRENCY);
            return;
        }

        if (currencyAliases.findCode(lowerText) != null) {
            currencyHandler.handle(user, text);
            return;
        }

        CompletableFuture.runAsync(() -> {
            String aiResponse = aiService.chat(text);
            String finalText = aiResponse != null ? aiResponse : "Напиши 'да', чтобы узнать курс еще одной валюты, или 'нет', чтобы попрощаться.";
            vkService.sendMessage(user.getVkUserId(), finalText);
        }, aiTaskExecutor);
    }
}