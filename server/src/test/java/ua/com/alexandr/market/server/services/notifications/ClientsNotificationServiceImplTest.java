package ua.com.alexandr.market.server.services.notifications;

import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ua.com.alexandr.market.server.model.AlertMessage;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class ClientsNotificationServiceImplTest {

    private ClientsNotificationService notificationService;
    private SimpMessagingTemplate messagingTemplate;
    private AlertMessage message;
    private static final String WS_ENDPOINT = "/test";

    @Before
    public void setUp() {
        messagingTemplate = mock(SimpMessagingTemplate.class);
        notificationService = new ClientsNotificationServiceImpl(messagingTemplate, WS_ENDPOINT);

        message = new AlertMessage(CurrencyPair.BTC_USD, new BigDecimal(500), new BigDecimal(3800), System.currentTimeMillis());
    }

    @Test
    public void notifyTest() {
        notificationService.notify(message);
        verify(messagingTemplate).convertAndSend(WS_ENDPOINT, message);
    }
}
