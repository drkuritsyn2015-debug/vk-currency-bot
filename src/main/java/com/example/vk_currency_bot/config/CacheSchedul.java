package com.example.vk_currency_bot.config;

import com.example.vk_currency_bot.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheSchedul {

    private final RequestService requestService;

    @Scheduled(cron = "0 15 0,12 * * *")
    @CacheEvict(value = "cbr_rates_all", allEntries = true)
    public void clearCbrCache() {
        log.info("Плановая очистка кэша 'cbr_rates_all' выполнена.");
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanOldHistory() {
        requestService.cleanOldHistory();
    }

}