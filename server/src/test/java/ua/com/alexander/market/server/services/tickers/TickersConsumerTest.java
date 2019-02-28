package ua.com.alexander.market.server.services.tickers;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import ua.com.alexander.market.server.model.AlertMessage;
import ua.com.alexander.market.server.services.notifications.ClientsNotificationService;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class TickersConsumerTest {

    private TickersConsumer tickersConsumer;
    private ClientsNotificationService clientsNotificationService;
    private Ticker ticker;
    private AlertMessage alertMessage;

    @Before
    public void setUp() {
        clientsNotificationService = mock(ClientsNotificationService.class);
        tickersConsumer = new TickersConsumer(clientsNotificationService);
        Date timestamp = new Date();
        ticker = new Ticker.Builder().currencyPair(CurrencyPair.BTC_USD).last(new BigDecimal(500)).timestamp(timestamp).build();
        alertMessage = new AlertMessage(CurrencyPair.BTC_USD, new BigDecimal(400), new BigDecimal(500), timestamp.getTime());
    }

    @Test
    public void acceptNewAlertMessageWithoutAnyLimitsShouldDoNothing() {
        tickersConsumer.accept(ticker);
        verifyZeroInteractions(clientsNotificationService);
    }

    @Test
    public void acceptNewAlertMessageWithLimitGreaterThanLastShouldDoNothing() {
        tickersConsumer.setLimit(CurrencyPair.BTC_USD, new BigDecimal(600));
        tickersConsumer.accept(ticker);
        verifyZeroInteractions(clientsNotificationService);
    }

    @Test
    public void acceptNewAlertMessageWithLimitLessThanLastShouldSendNotification() {
        tickersConsumer.setLimit(CurrencyPair.BTC_USD, new BigDecimal(400));
        tickersConsumer.accept(ticker);
        verify(clientsNotificationService).notify(alertMessage);
    }

    @Test
    public void acceptNewAlertMessageWithLimitEqualToLastShouldDoNothing() {
        tickersConsumer.setLimit(CurrencyPair.BTC_USD, new BigDecimal(500));
        tickersConsumer.accept(ticker);
        verifyZeroInteractions(clientsNotificationService);
    }
}
