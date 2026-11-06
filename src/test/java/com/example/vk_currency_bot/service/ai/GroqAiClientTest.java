package com.example.vk_currency_bot.service.ai;

import com.example.vk_currency_bot.config.AiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GroqAiClientTest {

    @Mock
    private RestClient aiRestClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private AiProperties aiProperties;

    @Spy
    private ObjectMapper objectMapper=new ObjectMapper();

    @InjectMocks
    private GroqAiClient groqAiClient;

    @BeforeEach
    void setUp() {
        when(aiProperties.getUrl()).thenReturn("https://api.groq.com/openai/v1/chat/completions");
        when(aiProperties.getModel()).thenReturn("llama-3.3-70b-versatile");
        when(aiProperties.getApiKey()).thenReturn("gsk_test_key");
    }

    @Test
    void chat_ShouldReturnContent_WhenGroqRespondsSuccessfully() {
        String fakeJsonResponse = "{\"choices\":[{\"message\":{\"content\":\"Привет, я Groq!\"}}]}";

        when(aiRestClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(fakeJsonResponse);

        String result = groqAiClient.chatAi("Тестовый промпт");

        assertEquals("Привет, я Groq!", result);
    }

    @Test
    void chat_ShouldReturnNull_WhenExceptionOccurs() {
        when(aiRestClient.post()).thenThrow(new RuntimeException("Network Error"));

        String result = groqAiClient.chatAi("Тестовый промпт");

        assertNull(result);
    }
}