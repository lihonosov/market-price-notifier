package ua.com.alexander.market.server.services.tickers.providers;

import info.bitrich.xchangestream.core.StreamingExchange;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class MarketTickerProvider implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketTickerProvider.class);
    private final StreamingExchange exchange;

    public MarketTickerProvider(StreamingExchange exchange) {
        this.exchange = exchange;
    }

    public Disposable subscribe(CurrencyPair currencyPair, Consumer<Ticker> tickerConsumer) {
        LOGGER.debug("[{}] Subscribing...", currencyPair);
        exchange.connect().blockingAwait();

        Disposable subscription = exchange.getStreamingMarketDataService()
                .getTicker(currencyPair)
                .subscribe(tickerConsumer, (exception) -> LOGGER.error("Error in subscribing tickers.", exception));
        LOGGER.debug("[{}] Subscribed", currencyPair);
        return subscription;
    }

    @Override
    public void close() {
        if (exchange != null) {
            exchange.disconnect().blockingAwait();
        }
    }
}
