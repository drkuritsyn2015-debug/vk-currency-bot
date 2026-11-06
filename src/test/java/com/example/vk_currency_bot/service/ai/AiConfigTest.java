package com.example.vk_currency_bot.service.ai;

import com.example.vk_currency_bot.config.AiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AiConfigTest {

    // Конфигурация, которая явно включает AiProperties для теста
    @Configuration
    @EnableConfigurationProperties(AiProperties.class)
    static class TestConfig {
    }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ConfigurationPropertiesAutoConfiguration.class))
            .withUserConfiguration(TestConfig.class)
            .withBean(ObjectMapper.class, ObjectMapper::new)
            .withBean("aiRestClient", RestClient.class, () -> mock(RestClient.class))
            .withBean(OllamaAiClient.class)
            .withBean(GroqAiClient.class);

    @Test
    void shouldLoadOllamaClient_WhenProviderIsOllama() {
        contextRunner
                .withPropertyValues(
                        "ai.provider=ollama",
                        "ai.url=http://localhost:11434/api/generate",
                        "ai.model=qwen",
                        "ai.prompt=test",
                        "ai.suffix=test"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(AiClient.class);
                    assertThat(context.getBean(AiClient.class)).isInstanceOf(OllamaAiClient.class);
                });
    }

    @Test
    void shouldLoadGroqClient_WhenProviderIsGroq() {
        contextRunner
                .withPropertyValues(
                        "ai.provider=groq",
                        "ai.url=https://api.groq.com/openai/v1/chat/completions",
                        "ai.model=llama",
                        "ai.prompt=test",
                        "ai.suffix=test",
                        "ai.apiKey=gsk_test"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(AiClient.class);
                    assertThat(context.getBean(AiClient.class)).isInstanceOf(GroqAiClient.class);
                });
    }


    @Test
    void shouldFail_WhenProviderIsUnknown() {
        contextRunner
                .withPropertyValues(
                        "ai.provider=unknown-provider",
                        "ai.url=http://localhost:11434/api/generate",
                        "ai.model=qwen",
                        "ai.prompt=test",
                        "ai.suffix=test",
                        "ai.apiKey=gsk_test"
                )
                .run(context -> {

                    assertThat(context).doesNotHaveBean(AiClient.class);
                });
    }
}


