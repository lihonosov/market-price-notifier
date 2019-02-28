package ua.com.alexander.market.server.services.subscriptions;

import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.alexander.market.server.services.tickers.TickerService;
import ua.com.alexander.market.server.services.tickers.TickersConsumer;

import java.math.BigDecimal;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Service
public class ClientSubscriptionServiceImpl implements ClientSubscriptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSubscriptionServiceImpl.class);

    private final TickerService tickerService;
    private final TickersConsumer tickersConsumer;

    @Autowired
    public ClientSubscriptionServiceImpl(TickerService tickerService, TickersConsumer tickersConsumer) {
        this.tickerService = tickerService;
        this.tickersConsumer = tickersConsumer;
    }

    @Override
    public void subscribe(CurrencyPair pair, BigDecimal limit) {
        tickersConsumer.setLimit(pair, limit);
        tickerService.subscribe(pair, tickersConsumer);
    }

    @Override
    public void unsubscribe(CurrencyPair pair, BigDecimal limit) {
        if (tickersConsumer.removeLimit(pair, limit)) {
            tickerService.unsubscribe(pair);
        } else {
            LOGGER.warn("[{}] Limit {} was not found", pair, limit);
        }
    }
}
