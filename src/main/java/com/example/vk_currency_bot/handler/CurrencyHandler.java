package com.example.vk_currency_bot.handler;

import com.example.vk_currency_bot.dto.cbr.ValuteDto;
import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyHandler implements MessageHandler {

    private final VkService vkService;
    private final UserService userService;
    private final CurrencyAliases currencyAliases;
    private final CbrService cbrService;
    private final RequestService requestService;

    @Override
    public DialogState getState() {
        return DialogState.WAITING_CURRENCY;
    }

    @Override
    public void handle(VkUser user, String text) {
        String currencyCode = currencyAliases.findCode(text);

        if (currencyCode == null) {
            vkService.sendMessage(user.getVkUserId(), "Извини, я не знаю такую валюту...");
            return;
        }


        List<ValuteDto> allRates = cbrService.getAllRates();


        Optional<ValuteDto> valuteOpt = cbrService.findRateInList(allRates, currencyCode);

        if (valuteOpt.isEmpty()) {
            vkService.sendMessage(user.getVkUserId(), "Извини, не удалось получить курс валюты из ЦБ. Попробуй позже.");
            return;
        }

        ValuteDto valute = valuteOpt.get();
        BigDecimal rate = valute.getValue();
        String currencyName = currencyAliases.getName(currencyCode);

        String formattedRate = rate.setScale(2, java.math.RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        String response = String.format("%s на сегодня оценивается в %s рублей. Ещё что-нибудь?", currencyName, formattedRate);


        vkService.sendMessage(user.getVkUserId(), response);

        try {
            requestService.saveRequest(user.getVkUserId(), text, currencyCode, rate);
        } catch (Exception e) {
            log.error("Не удалось сохранить историю запроса в БД.", e);
        }

        userService.updateState(user, DialogState.WAITING_MORE);
    }
}




