package ua.com.alexandr.market.server.services.tickers;

import io.reactivex.functions.Consumer;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public interface TickerService extends AutoCloseable {
    void subscribe(CurrencyPair currencyPair, Consumer<Ticker> tickerConsumer);

    void unsubscribe(CurrencyPair currencyPair);
}
