package com.example.vk_currency_bot.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class CurrencyAliases {

    private final Map<String, String> aliases = new HashMap<>();

    public CurrencyAliases() {
        // ===== ПОПУЛЯРНЫЕ ВАЛЮТЫ =====
        aliases.put("доллар", "USD");
        aliases.put("доллары", "USD");
        aliases.put("бакс", "USD");
        aliases.put("баксы", "USD");
        aliases.put("usd", "USD");
        aliases.put("$", "USD");
        aliases.put("американский", "USD");

        aliases.put("евро", "EUR");
        aliases.put("еврик", "EUR");
        aliases.put("eur", "EUR");
        aliases.put("€", "EUR");
        aliases.put("европейский", "EUR");

        aliases.put("юань", "CNY");
        aliases.put("юани", "CNY");
        aliases.put("cny", "CNY");
        aliases.put("китайский", "CNY");
        aliases.put("китайская", "CNY");
        aliases.put("китай", "CNY");

        aliases.put("фунт", "GBP");
        aliases.put("фунты", "GBP");
        aliases.put("gbp", "GBP");
        aliases.put("английский", "GBP");
        aliases.put("стерлингов", "GBP");

        aliases.put("йена", "JPY");
        aliases.put("иена", "JPY");
        aliases.put("jpy", "JPY");
        aliases.put("японский", "JPY");

        aliases.put("тенге", "KZT");
        aliases.put("kzt", "KZT");
        aliases.put("казахстан", "KZT");
        aliases.put("казахстанский", "KZT");

        aliases.put("белорусский", "BYN");
        aliases.put("беларусь", "BYN");
        aliases.put("byn", "BYN");

        // ===== ОСТАЛЬНЫЕ ВАЛЮТЫ ЦБ =====
        aliases.put("австралийский", "AUD");
        aliases.put("aud", "AUD");

        aliases.put("азербайджанский", "AZN");
        aliases.put("манат", "AZN");
        aliases.put("azn", "AZN");

        aliases.put("армянский", "AMD");
        aliases.put("драм", "AMD");
        aliases.put("amd", "AMD");

        aliases.put("болгарский", "BGN");
        aliases.put("лев", "BGN");
        aliases.put("bgn", "BGN");

        aliases.put("бразильский", "BRL");
        aliases.put("реал", "BRL");
        aliases.put("brl", "BRL");

        aliases.put("венгерский", "HUF");
        aliases.put("форинт", "HUF");
        aliases.put("huf", "HUF");

        aliases.put("датский", "DKK");
        aliases.put("крона", "DKK"); // Датская крона
        aliases.put("dkk", "DKK");

        aliases.put("индийский", "INR");
        aliases.put("рупий", "INR");
        aliases.put("рупия", "INR");
        aliases.put("inr", "INR");

        aliases.put("канадский", "CAD");
        aliases.put("cad", "CAD");

        aliases.put("киргизский", "KGS");
        aliases.put("сом", "KGS");
        aliases.put("kgs", "KGS");

        aliases.put("молдавский", "MDL");
        aliases.put("лей", "MDL");
        aliases.put("mdl", "MDL");

        aliases.put("норвежский", "NOK");
        aliases.put("nok", "NOK"); // Норвежская крона

        aliases.put("польский", "PLN");
        aliases.put("злотый", "PLN");
        aliases.put("pln", "PLN");

        aliases.put("румынский", "RON");
        aliases.put("ron", "RON");

        aliases.put("сингапурский", "SGD");
        aliases.put("sgd", "SGD");

        aliases.put("таджикский", "TJS");
        aliases.put("сомони", "TJS");
        aliases.put("tjs", "TJS");

        aliases.put("турецкий", "TRY");
        aliases.put("лира", "TRY");
        aliases.put("try", "TRY");

        aliases.put("туркменский", "TMT");
        aliases.put("tmt", "TMT");

        aliases.put("узбекский", "UZS");
        aliases.put("сум", "UZS");
        aliases.put("uzs", "UZS");

        aliases.put("украинский", "UAH");
        aliases.put("гривна", "UAH");
        aliases.put("uah", "UAH");

        aliases.put("чешский", "CZK");
        aliases.put("czk", "CZK"); // Чешская крона

        aliases.put("шведский", "SEK");
        aliases.put("sek", "SEK"); // Шведская крона

        aliases.put("швейцарский", "CHF");
        aliases.put("франк", "CHF");
        aliases.put("chf", "CHF");

        aliases.put("южноафриканский", "ZAR");
        aliases.put("рэнд", "ZAR");
        aliases.put("zar", "ZAR");

        aliases.put("вона", "KRW");
        aliases.put("krw", "KRW");
    }

    public String findCode(String input) {
        if (input == null) return null;
        String lower = input.toLowerCase(Locale.ROOT).trim();


        String code = aliases.get(lower);
        if (code != null) return code;

        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    public String getName(String code) {
        switch (code) {
            case "USD": return "Доллар";
            case "EUR": return "Евро";
            case "CNY": return "Юань";
            case "GBP": return "Фунт";
            case "JPY": return "Йена";
            case "KZT": return "Тенге";
            case "BYN": return "Белорусский рубль";
            case "TRY": return "Турецкая лира";
            case "UZS": return "Узбекский сум";
            case "AMD": return "Армянский драм";
            case "AZN": return "Азербайджанский манат";
            case "GEL": return "Грузинский лари"; // Если ЦБ его котирует
            case "INR": return "Индийский рупий";
            default: return code;
        }
    }

    public String getAvailableCurrencies() {
        return "доллар, евро, юань, фунт, йена, тенге, белорусский рубль, лира, сум, драм, манат, рупий, франк, злотый";
    }
}