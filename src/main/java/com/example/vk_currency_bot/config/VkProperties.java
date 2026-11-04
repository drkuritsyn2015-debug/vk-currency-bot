package com.example.vk_currency_bot.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConfigurationProperties(prefix = "vk")
public class VkProperties {

    @NotBlank(message = "ID группы не может быть пустым")
    private final String groupId;

    @NotBlank(message = "Токен доступа не может быть пустым")
    private final String accessToken;

    @NotBlank(message = "Версия API не может быть пустой")
    private final String apiVersion;

    @NotBlank(message = "адресс не может быть пустым")
    private final String apiUrl;

    @NotNull(message = "Время ожидания LongPoll не может быть null")
    @Min(value = 1, message = "Время ожидания должно быть больше 0")
    private final Integer longpollWait;

    public VkProperties(String groupId, String accessToken, String apiVersion, Integer longpollWait,String apiUrl) {
        this.groupId = groupId;
        this.accessToken = accessToken;
        this.apiVersion = apiVersion;
        this.longpollWait = longpollWait;
        this.apiUrl=apiUrl;
    }
}