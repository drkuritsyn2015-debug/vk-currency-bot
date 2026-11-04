package com.example.vk_currency_bot.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConfigurationProperties(prefix = "cbr")
public class CbrProperties {
    @NotBlank
    private final String url;

    public CbrProperties(String url) {
        this.url = url;
    }
}