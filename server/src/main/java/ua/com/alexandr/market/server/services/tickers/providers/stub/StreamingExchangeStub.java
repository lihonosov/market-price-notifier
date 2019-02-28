package ua.com.alexandr.market.server.services.tickers.providers.stub;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.Completable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.mazi.rescu.SynchronizedValueFactory;

import java.util.List;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class StreamingExchangeStub implements StreamingExchange {
    private final StreamingMarketDataServiceStub marketDataServiceStub;

    @Autowired
    public StreamingExchangeStub(StreamingMarketDataServiceStub marketDataServiceStub) {
        this.marketDataServiceStub = marketDataServiceStub;
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        return Completable.complete();
    }

    @Override
    public Completable disconnect() {
        return Completable.complete();
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return marketDataServiceStub;
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {

    }

    @Override
    public ExchangeSpecification getExchangeSpecification() {
        return null;
    }

    @Override
    public ExchangeMetaData getExchangeMetaData() {
        return null;
    }

    @Override
    public List<CurrencyPair> getExchangeSymbols() {
        return null;
    }

    @Override
    public SynchronizedValueFactory<Long> getNonceFactory() {
        return null;
    }

    @Override
    public ExchangeSpecification getDefaultExchangeSpecification() {
        return null;
    }

    @Override
    public void applySpecification(ExchangeSpecification exchangeSpecification) {

    }

    @Override
    public MarketDataService getMarketDataService() {
        return null;
    }

    @Override
    public TradeService getTradeService() {
        return null;
    }

    @Override
    public AccountService getAccountService() {
        return null;
    }

    @Override
    public void remoteInit() throws ExchangeException {

    }
}
