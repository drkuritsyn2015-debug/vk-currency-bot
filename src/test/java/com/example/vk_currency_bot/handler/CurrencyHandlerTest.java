package com.example.vk_currency_bot.handler;

import com.example.vk_currency_bot.dto.cbr.ValuteDto;
import com.example.vk_currency_bot.entity.DialogState;
import com.example.vk_currency_bot.entity.VkUser;
import com.example.vk_currency_bot.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyHandlerTest {

    @Mock
    private VkService vkService;
    @Mock
    private UserService userService;
    @Mock
    private CurrencyAliases currencyAliases;
    @Mock
    private CbrService cbrService;
    @Mock
    private RequestService requestService;

    @InjectMocks
    private CurrencyHandler currencyHandler;

    private VkUser testUser;

    @BeforeEach
    void setUp() {
        testUser = VkUser.builder()
                .vkUserId(123L)
                .dialogState(DialogState.WAITING_CURRENCY)
                .build();
    }

    @Test
    void shouldSendRateAndUpdateState_WhenCurrencyIsValid() {

        when(currencyAliases.findCode("доллар")).thenReturn("USD");
        when(currencyAliases.getName("USD")).thenReturn("Доллар");

        ValuteDto mockValute = mock(ValuteDto.class);
        when(mockValute.getValue()).thenReturn(new BigDecimal("80.54"));

        List<ValuteDto> mockRates = List.of(mockValute);


        when(cbrService.getAllRates()).thenReturn(mockRates);
        when(cbrService.findRateInList(anyList(), eq("USD"))).thenReturn(Optional.of(mockValute));


        currencyHandler.handle(testUser, "доллар");


        verify(vkService).sendMessage(eq(123L), contains("80.54"));
        verify(requestService).saveRequest(eq(123L), eq("доллар"), eq("USD"), any(BigDecimal.class));
        verify(userService).updateState(eq(testUser), eq(DialogState.WAITING_MORE));
    }

    @Test
    void shouldSendErrorMessage_WhenCurrencyIsInvalid() {
        when(currencyAliases.findCode("абракадабра")).thenReturn(null);

        currencyHandler.handle(testUser, "абракадабра");

        verify(vkService).sendMessage(eq(123L), contains("не знаю такую валюту"));
        verifyNoInteractions(cbrService);
        verifyNoInteractions(requestService);
    }
}