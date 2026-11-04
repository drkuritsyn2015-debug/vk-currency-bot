package com.example.vk_currency_bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CurrencyAliasesTest {

    private CurrencyAliases currencyAliases;

    @BeforeEach
    void setUp() {
        currencyAliases = new CurrencyAliases();
    }

    @Test
    void testFindCode_WhenInputIsExactWord_ShouldReturnCode() {

        String result = currencyAliases.findCode("доллар");

        assertEquals("USD", result);
    }

    @Test
    void testFindCode_WhenInputHasUpperCase_ShouldReturnCode() {
        String result = currencyAliases.findCode("ЕВРО");
        assertEquals("EUR", result);
    }

    @Test
    void testFindCode_WhenInputIsPhrase_ShouldReturnCode() {
        String result = currencyAliases.findCode("какой курс еврика  сегодня?");
        assertEquals("EUR", result);
    }

    @Test
    void testFindCode_WhenInputIsUnknown_ShouldReturnNull() {
        String result = currencyAliases.findCode("абракадабра");
        assertNull(result);
    }
}