package com.example.vk_currency_bot.service;

import com.example.vk_currency_bot.config.CbrProperties;
import com.example.vk_currency_bot.dto.cbr.ValuteDto;
import com.example.vk_currency_bot.dto.cbr.ValuteResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CbrService {

    private final XmlMapper xmlMapper;
    private final CbrProperties cbrProperties;

    @Qualifier("fastRestClient")
    private final RestClient fastRestClient;

    @Cacheable(value = "cbr_rates_all")
    @Retryable(
            retryFor = ResourceAccessException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<ValuteDto> getAllRates() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String url = String.format(cbrProperties.getUrl(), today);

        log.info("Запрос ВСЕХ курсов ЦБ на дату: {}", today);

        try {
            String xmlResponse = fastRestClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            ValuteResponse response = xmlMapper.readValue(xmlResponse, ValuteResponse.class);

            if (response == null || response.getValutes() == null) {
                return Collections.emptyList();
            }
            return response.getValutes();

        } catch (ResourceAccessException e) {
            log.warn("Сетевая ошибка при запросе к ЦБ. Пробуем переподключиться...");
            throw e;
        } catch (Exception e) {
            log.error("Ошибка парсинга XML от ЦБ", e);
            return Collections.emptyList();
        }
    }

    public Optional<ValuteDto> findRateInList(List<ValuteDto> rates, String currencyCode) {
        return rates.stream()
                .filter(v -> v.getCharCode().equalsIgnoreCase(currencyCode))
                .findFirst();
    }
}






