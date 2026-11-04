package com.example.vk_currency_bot.dto.cbr;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class CbrBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        String value = p.getText();

        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }


        String normalized = value.trim().replace(',', '.');
        return new BigDecimal(normalized);
    }
}








