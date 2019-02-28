package ua.com.alexandr.market.server.services.subscriptions;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import ua.com.alexandr.market.server.services.notifications.ClientsNotificationService;
import ua.com.alexandr.market.server.services.tickers.TickerService;
import ua.com.alexandr.market.server.services.tickers.TickersConsumer;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class ClientSubscriptionServiceImplTest {

    private ClientSubscriptionService clientSubscriptionService;
    private TickerService tickerService;
    private TickersConsumer tickerConsumer;

    @Before
    public void setUp() {
        tickerService = mock(TickerService.class);
        ClientsNotificationService clientsNotifier = mock(ClientsNotificationService.class);
        tickerConsumer = spy(new TickersConsumer(clientsNotifier));
        clientSubscriptionService = new ClientSubscriptionServiceImpl(tickerService, tickerConsumer);
    }

    @Test
    public void subscribe() {
        clientSubscriptionService.subscribe(CurrencyPair.BTC_USD, new BigDecimal(500));

        verify(tickerConsumer, times(1)).setLimit(CurrencyPair.BTC_USD, new BigDecimal(500));
        verify(tickerService, times(1)).subscribe(CurrencyPair.BTC_USD, tickerConsumer);
    }

    @Test
    public void subscribeMultipleTimesAndOverrideLimit() {
        CurrencyPair currencyPair = CurrencyPair.BTC_USD;
        BigDecimal firstLimit = new BigDecimal(500);
        BigDecimal secondLimit = new BigDecimal(700);
        clientSubscriptionService.subscribe(currencyPair, firstLimit);
        clientSubscriptionService.subscribe(currencyPair, secondLimit);

        verify(tickerConsumer, times(1)).setLimit(currencyPair, firstLimit);
        verify(tickerConsumer, times(1)).setLimit(currencyPair, secondLimit);
        verify(tickerService, times(2)).subscribe(currencyPair, tickerConsumer);
    }

    @Test
    public void unsubscribeWithoutAnySubscriptions() {
        clientSubscriptionService.unsubscribe(CurrencyPair.BTC_USD, new BigDecimal(500));

        verify(tickerConsumer, times(1)).removeLimit(CurrencyPair.BTC_USD, new BigDecimal(500));
        verifyZeroInteractions(tickerService);
    }

    @Test
    public void unsubscribeExistingSubscription() {
        CurrencyPair currencyPair = CurrencyPair.BTC_USD;
        BigDecimal limit = new BigDecimal(500);
        clientSubscriptionService.subscribe(currencyPair, limit);
        clientSubscriptionService.unsubscribe(currencyPair, limit);

        verify(tickerConsumer, times(1)).setLimit(currencyPair, limit);
        verify(tickerService, times(1)).subscribe(currencyPair, tickerConsumer);

        verify(tickerConsumer, times(1)).removeLimit(currencyPair, limit);
        verify(tickerService, times(1)).unsubscribe(currencyPair);
    }

    @Test
    public void subscribeAndUnsubscribeUnknownLimit() {
        CurrencyPair currencyPair = CurrencyPair.BTC_USD;
        BigDecimal limit = new BigDecimal(500);
        BigDecimal unknownLimit = new BigDecimal(700);

        clientSubscriptionService.subscribe(currencyPair, limit);
        clientSubscriptionService.unsubscribe(currencyPair, unknownLimit);

        verify(tickerConsumer, times(1)).setLimit(currencyPair, limit);
        verify(tickerConsumer, times(1)).removeLimit(currencyPair, unknownLimit);
        verify(tickerService, times(1)).subscribe(currencyPair, tickerConsumer);
    }
}
