package com.example.vk_currency_bot.handler;

import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;

public interface MessageHandler {
    DialogState getState();
    void handle(VkUser user, String text);
}