package com.example.vk_currency_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
@ConfigurationPropertiesScan
@EnableCaching
@EnableScheduling
public class VkCurrencyBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(VkCurrencyBotApplication.class, args);
	}

}
