package com.example.vk_currency_bot.handler;

import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.service.CurrencyAliases;
import com.example.vk_currency_bot.service.UserService;
import com.example.vk_currency_bot.service.VkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartHandler implements MessageHandler {

    private final VkService vkService;
    private final UserService userService;
    private final CurrencyAliases currencyAliases;

    @Override
    public DialogState getState() {
        return DialogState.NEW_USER;
    }

    @Override
    public void handle(VkUser user, String text) {

        String fullName = vkService.getUserName(user.getVkUserId());


        String[] nameParts = fullName.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";


        userService.updateProfile(user, firstName, lastName);


        vkService.sendMessage(user.getVkUserId(),
                "Привет, " + firstName + "! Я бот Арнольд. Какая валюта тебя интересует? ("
                        + currencyAliases.getAvailableCurrencies() + ")");

        userService.updateState(user, DialogState.WAITING_CURRENCY);
    }
}