package ua.com.alexander.market.server.services.tickers.providers;

import info.bitrich.xchangestream.bitfinex.BitfinexStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
@Profile("!test")
public class BitfinexMarketTickerProvider extends MarketTickerProvider {
    public BitfinexMarketTickerProvider() {
        super(StreamingExchangeFactory.INSTANCE.createExchange(BitfinexStreamingExchange.class.getName()));
    }
}
