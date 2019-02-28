package ua.com.alexander.market.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.mockito.MockingDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.alexander.market.client.Client;
import ua.com.alexander.market.client.ClientApplication;
import ua.com.alexander.market.client.ClientConfiguration;
import ua.com.alexander.market.client.connector.ClientSubscriber;
import ua.com.alexander.market.client.handler.ClientMessageConsumer;
import ua.com.alexander.market.client.model.AlertMessage;
import ua.com.alexander.market.server.ServerApplication;
import ua.com.alexander.market.server.ServerConfiguration;
import ua.com.alexander.market.server.services.notifications.ClientsNotificationService;
import ua.com.alexander.market.server.services.tickers.TickersConsumer;
import ua.com.alexander.market.server.services.tickers.providers.stub.StreamingMarketDataServiceStub;

import java.math.BigDecimal;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServerConfiguration.class, ClientConfiguration.class})
@SpringBootTest(classes = {ServerApplication.class, ClientApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ClientServerIntegrationTest {

    @Autowired
    private Client client;

    @Autowired
    private ClientSubscriber clientSubscriber;

    @SpyBean
    private ClientMessageConsumer messageConsumer;

    @SpyBean
    private TickersConsumer tickersConsumer;

    @SpyBean
    private ClientsNotificationService notificationService;

    @Autowired
    private StreamingMarketDataServiceStub dataServiceStub;

    private String currencyPair;
    private BigDecimal limit;

    @Before
    public void setUp() {
        await().atMost(5, SECONDS).until(clientSubscriber::isSubscribed);
        reset(messageConsumer, tickersConsumer, notificationService);
    }

    @Test
    public void clientShouldNotReceiveMessagesWithoutLimit() {
        dataServiceStub.generate(CurrencyPair.BTC_PLN, 100, 500, 100);
        verifyZeroInteractions(tickersConsumer);
        verifyZeroInteractions(notificationService);
        verifyZeroInteractions(messageConsumer);
    }

    @Test
    public void clientShouldNotReceiveMessagesWithGreaterLimit() {
        currencyPair = "BTC-GBP";
        limit = new BigDecimal(9999);

        client.setAlert(currencyPair, limit);
        dataServiceStub.generate(CurrencyPair.BTC_GBP, 100, 500, 100);

        awaitAndCheck(mockingDetails(tickersConsumer), 6);

        verify(tickersConsumer, times(1)).setLimit(CurrencyPair.BTC_GBP, limit);
        verifyZeroInteractions(notificationService);
        verifyZeroInteractions(messageConsumer);
    }

    @Test
    public void clientShouldReceiveMessagesWithLimit() {
        currencyPair = "BTC-USD";
        limit = new BigDecimal(222);

        client.setAlert(currencyPair, limit);
        dataServiceStub.generate(CurrencyPair.BTC_USD, 100, 1000, 100);

        awaitAndCheck(mockingDetails(tickersConsumer), 11);
        awaitAndCheck(mockingDetails(notificationService), 8);
        awaitAndCheck(mockingDetails(messageConsumer), 8);

        verify(tickersConsumer, times(1)).setLimit(CurrencyPair.BTC_USD, limit);
        verify(notificationService, times(8)).notify(any(ua.com.alexander.market.server.model.AlertMessage.class));
        verify(messageConsumer, times(8)).accept(any(AlertMessage.class));
    }

    @Test
    public void clientShouldNotReceiveMessagesAfterRemoveLimit() {
        currencyPair = "BTC-EUR";
        limit = new BigDecimal(212);

        client.setAlert(currencyPair, limit);
        dataServiceStub.generate(CurrencyPair.BTC_EUR, 100, 1000, 100);

        awaitAndCheck(mockingDetails(tickersConsumer), 11);
        awaitAndCheck(mockingDetails(notificationService), 8);
        awaitAndCheck(mockingDetails(messageConsumer), 8);

        verify(tickersConsumer, times(1)).setLimit(CurrencyPair.BTC_EUR, limit);
        reset(messageConsumer, tickersConsumer, notificationService);

        client.removeAlert(currencyPair, limit);

        dataServiceStub.generate(CurrencyPair.BTC_EUR, 100, 1000, 100);

        verify(tickersConsumer, times(1)).removeLimit(CurrencyPair.BTC_EUR, limit);
        verifyZeroInteractions(notificationService);
        verifyZeroInteractions(messageConsumer);
    }

    @After
    public void tearDown() {
        if (currencyPair != null && limit != null) {
            client.removeAlert(currencyPair, limit);
        }
    }

    private void awaitAndCheck(MockingDetails mockingDetails, int execCount) {
        await().atMost(5, SECONDS).until(() -> mockingDetails.getInvocations().size() == execCount);
    }
}
