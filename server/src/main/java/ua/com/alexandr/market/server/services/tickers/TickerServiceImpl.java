package ua.com.alexandr.market.server.services.tickers;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.alexandr.market.server.services.tickers.providers.MarketTickerProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Service
public class TickerServiceImpl implements TickerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TickerServiceImpl.class);

    private Map<CurrencyPair, Disposable> subscriptions = new HashMap<>();
    private MarketTickerProvider tickerProvider;

    @Autowired
    public TickerServiceImpl(MarketTickerProvider tickerProvider) {
        this.tickerProvider = tickerProvider;
    }

    @Override
    public void subscribe(CurrencyPair currencyPair, Consumer<Ticker> tickerConsumer) {
        Disposable subscription = subscriptions.get(currencyPair);
        if (subscription != null && !subscription.isDisposed()) {
            LOGGER.info("[{}] Server already subscribed!", currencyPair);
            return;
        }
        subscription = tickerProvider.subscribe(currencyPair, tickerConsumer);
        subscriptions.put(currencyPair, subscription);
    }

    @Override
    public void unsubscribe(CurrencyPair currencyPair) {
        Disposable subscription = subscriptions.get(currencyPair);
        if (subscription == null || subscription.isDisposed()) {
            LOGGER.warn("[{}] Server already subscribed or subscription was disposed!", currencyPair);
        } else {
            subscription.dispose();
            subscriptions.remove(currencyPair);
            LOGGER.debug("[{}] Unsubscribed", currencyPair);
        }
    }

    @Override
    public void close() {
        LOGGER.debug("Disconnecting...");
        subscriptions.forEach(this::closeSubscription);
    }

    private void closeSubscription(CurrencyPair currencyPair, Disposable subscription) {
        try {
            subscription.dispose();
            LOGGER.debug("[{}] Subscription closed", currencyPair);
        } catch (Exception e) {
            LOGGER.error("[{}] Error closing subscription: {}", currencyPair, e.getMessage());
        }
    }
}
