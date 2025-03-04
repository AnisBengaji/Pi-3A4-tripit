package org.projeti.utils;


import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final Map<String, Double> rates = new HashMap<>();

    static {
        rates.put("TND", 1.0);
        rates.put("USD", 1.0 / 2.8);
        rates.put("EUR", 0.92 / 2.8);
        rates.put("CAD", 1.35 / 2.8);
    }

    public static double convert(double amount, String fromCurrency, String toCurrency) {
        if (!rates.containsKey(fromCurrency) || !rates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported currency code.");
        }
        double amountInBase = amount / rates.get(fromCurrency);
        return amountInBase * rates.get(toCurrency);
    }
}
