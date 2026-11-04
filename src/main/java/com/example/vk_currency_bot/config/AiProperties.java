package com.example.vk_currency_bot.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

    @NotBlank(message = "адресс ии должен быть указан")
    private final String url;

    @NotBlank(message = "модель ии нужна для соединения")
    private final String model;

    @NotBlank(message = "промпт не обязателен, но лучше проверить")
    private final String prompt;

    @NotBlank(message = "суфикс не обязателен, но лучше проверить")
    private final String suffix;

    public AiProperties(String url, String model, String prompt, String suffix) {
        this.url = url;
        this.model = model;
        this.prompt = prompt;
        this.suffix = suffix;
    }
}