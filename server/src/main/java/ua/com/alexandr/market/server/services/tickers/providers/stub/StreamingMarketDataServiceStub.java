package ua.com.alexandr.market.server.services.tickers.providers.stub;

import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.Observable;
import org.apache.commons.lang3.time.DateUtils;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class StreamingMarketDataServiceStub implements StreamingMarketDataService {
    private List<Ticker> tickers = new ArrayList<>();
    private Subscriber<? super Ticker> lastSubscriber;

    public synchronized void generate(CurrencyPair currencyPair, int start, int max, int step) {
        Date timestamp = DateUtils.addDays(new Date(), -20);
        for (int i = start; i <= max; i += step) {
            if (i % 3 == 0) {
                tickers.add(new Ticker.Builder().currencyPair(currencyPair).last(new BigDecimal(i - 50)).timestamp(DateUtils.addSeconds(timestamp, i)).build());
            } else {
                tickers.add(new Ticker.Builder().currencyPair(currencyPair).last(new BigDecimal(i)).timestamp(DateUtils.addSeconds(timestamp, i)).build());
            }
        }
        if (lastSubscriber != null) {
            publish(lastSubscriber);
            tickers.clear();
        }
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        return Observable.empty();
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        Publisher<Ticker> publisher = this::publish;
        return Observable.fromPublisher(publisher);
    }

    private synchronized void publish(Subscriber<? super Ticker> subscriber) {
        this.lastSubscriber = subscriber;
        tickers.forEach(subscriber::onNext);
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        return Observable.empty();
    }
}
