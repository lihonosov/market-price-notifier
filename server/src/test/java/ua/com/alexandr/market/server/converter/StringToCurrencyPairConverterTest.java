package ua.com.alexandr.market.server.converter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knowm.xchange.currency.CurrencyPair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class StringToCurrencyPairConverterTest {

    private StringToCurrencyPairConverter converter;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        converter = new StringToCurrencyPairConverter();
    }

    @Test
    public void convertNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Currency pair is required");
        converter.convert(null);
    }

    @Test
    public void convertEmpty() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Currency pair is required");
        converter.convert("");
    }

    @Test
    public void convertNonValidFormat() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Unsupported currency pair format: xxx. Should be something like: BTC-USD");
        converter.convert("xxx");
    }

    @Test
    public void convertValid() {
        CurrencyPair currencyPair = converter.convert("BTC-USD");
        assertNotNull(currencyPair);
        assertEquals("BTC", currencyPair.base.toString());
        assertEquals("USD", currencyPair.counter.toString());
    }
}
