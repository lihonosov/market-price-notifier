package ua.com.alexandr.market.server.services.tickers.providers.stub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ua.com.alexandr.market.server.services.tickers.providers.MarketTickerProvider;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
@Profile("test")
public class MarketTickerProviderStub extends MarketTickerProvider {
    @Autowired
    public MarketTickerProviderStub(StreamingExchangeStub exchangeStub) {
        super(exchangeStub);
    }
}


