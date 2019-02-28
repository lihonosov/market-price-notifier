package ua.com.alexander.market.server.converter;

import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.core.convert.converter.Converter;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class StringToCurrencyPairConverter implements Converter<String, CurrencyPair> {
    private static final String DELIMITER = "-";

    @Override
    public CurrencyPair convert(String value) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException("Currency pair is required");
        }
        if (!value.matches("([a-zA-Z]+)-([a-zA-Z]+)")) {
            throw new IllegalArgumentException("Unsupported currency pair format: " + value + ". Should be something like: BTC-USD");
        }
        String[] pairs = value.split(DELIMITER);
        return new CurrencyPair(pairs[0], pairs[1]);
    }
}
