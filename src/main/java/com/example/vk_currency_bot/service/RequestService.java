package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.entity.RequestHistory;
import com.example.vk_currency_bot.repository.RequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestHistoryRepository historyRepository;

    @Transactional
    public void saveRequest(Long vkUserId, String requestText, String currencyCode, BigDecimal rateValue) {

        RequestHistory history = RequestHistory.builder()
                .vkUserId(vkUserId)
                .requestText(requestText)
                .currencyCode(currencyCode)
                .rateValue(rateValue)
                .requestDate(LocalDateTime.now())
                .build();

        historyRepository.save(history);
    }

    @Transactional
    public void cleanOldHistory() {
        LocalDateTime olderThan30days = LocalDateTime.now().minusDays(10);
        int count = historyRepository.deleteOldRequests(olderThan30days);
        if (count > 0) {
            log.info("очистка БД. Все что старше 10 дней: {} строк", count);
        }
    }
}