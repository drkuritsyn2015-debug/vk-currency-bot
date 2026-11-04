package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.handler.HistoryHandler;
import com.example.vk_currency_bot.handler.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DialogService {

    private final UserService userService;
    private final Map<DialogState, MessageHandler> handlers;
    private final HistoryHandler historyHandler;

    public DialogService(UserService userService, List<MessageHandler> handlerList, HistoryHandler historyHandler) {
        this.userService = userService;
        this.historyHandler = historyHandler;
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(MessageHandler::getState, Function.identity()));
    }

    public void processIncomingMessage(Long peerId, String text) {
        VkUser user = userService.getOrCreateUser(peerId);

        if (text.equalsIgnoreCase("/history") || text.equalsIgnoreCase("история")) {
            historyHandler.handle(user, text);
            return;
        }

        MessageHandler handler = handlers.get(user.getDialogState());
        if (handler != null) {
            handler.handle(user, text);
        }
    }
}
