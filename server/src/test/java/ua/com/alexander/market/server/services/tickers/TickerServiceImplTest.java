package ua.com.alexander.market.server.services.tickers;

import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import ua.com.alexander.market.server.services.notifications.ClientsNotificationService;
import ua.com.alexander.market.server.services.tickers.providers.MarketTickerProvider;
import ua.com.alexander.market.server.services.tickers.providers.stub.MarketTickerProviderStub;
import ua.com.alexander.market.server.services.tickers.providers.stub.StreamingExchangeStub;
import ua.com.alexander.market.server.services.tickers.providers.stub.StreamingMarketDataServiceStub;

import static org.mockito.Mockito.*;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class TickerServiceImplTest {

    private TickerService tickerService;
    private Consumer<Ticker> tickerConsumer;
    private MarketTickerProvider tickerProvider;

    @Before
    public void setUp() {
        StreamingMarketDataServiceStub marketDataServiceStub = new StreamingMarketDataServiceStub();
        marketDataServiceStub.generate(CurrencyPair.BTC_USD, 500, 1000, 100);
        tickerProvider = spy(new MarketTickerProviderStub(new StreamingExchangeStub(marketDataServiceStub)));

        ClientsNotificationService clientNotificationService = mock(ClientsNotificationService.class);
        tickerConsumer = spy(new TickersConsumer(clientNotificationService));

        tickerService = new TickerServiceImpl(tickerProvider);
    }

    @Test
    public void shouldCreateNewSubscription() {
        tickerService.subscribe(CurrencyPair.BTC_USD, tickerConsumer);

        verify(tickerProvider, times(1)).subscribe(CurrencyPair.BTC_USD, tickerConsumer);
    }

    @Test
    public void shouldDoNothingWhenSubscriptionAlreadyExists() {
        tickerService.subscribe(CurrencyPair.BTC_USD, tickerConsumer);
        tickerService.subscribe(CurrencyPair.BTC_USD, tickerConsumer);

        verify(tickerProvider, times(1)).subscribe(CurrencyPair.BTC_USD, tickerConsumer);
    }

    @Test
    public void shouldDoNothingWhenNoSubscriptions() {
        tickerService.unsubscribe(CurrencyPair.BTC_USD);

        verifyZeroInteractions(tickerProvider);
        verifyZeroInteractions(tickerConsumer);
    }
}
