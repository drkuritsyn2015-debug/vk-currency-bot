package com.example.vk_currency_bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerPing {

    /**
     * Для специфической работы с Сron-Job.org
     * и Render.com
     * @return 2 байта
     */
    @GetMapping("/ping")
    public String ping() {
        return "OK"; // Всего 2 байта
    }
}