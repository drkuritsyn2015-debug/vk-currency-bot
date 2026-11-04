package com.example.vk_currency_bot.handler;

import com.example.vk_currency_bot.entity.RequestHistory;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.repository.RequestHistoryRepository;
import com.example.vk_currency_bot.service.VkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryHandler {

    private final RequestHistoryRepository repository;
    private final VkService vkService;


    public void handle(VkUser user, String text) {

        List<RequestHistory> history = repository.findTop5ByVkUserIdOrderByRequestDateDesc(user.getVkUserId());

        if (history.isEmpty()) {
            vkService.sendMessage(user.getVkUserId(), "Вы еще не запрашивали курсы.");
            return;
        }

        StringBuilder sb = new StringBuilder("Ваши последние запросы:\n");


        for (RequestHistory h : history) {
            String formattedRate = h.getRateValue()
                    .setScale(2, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString();

            sb.append("• ").append(h.getCurrencyCode()).append(": ").append(formattedRate).append(" руб.\n");
        }

        vkService.sendMessage(user.getVkUserId(), sb.toString());
    }
}