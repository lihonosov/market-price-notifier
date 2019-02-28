package ua.com.alexander.market.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ua.com.alexander.market.client.connector.ClientSubscriber;
import ua.com.alexander.market.client.handler.ClientMessageConsumer;
import ua.com.alexander.market.client.model.AlertMessage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ClientConfiguration.class, ClientApplicationIntegrationTest.ServerStubConfiguration.class})
@SpringBootTest(classes = ClientApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClientApplicationIntegrationTest {

    @Value("${market.server.ws-url}")
    private String wsUrl;

    @Value("${market.server.ws-subscription-uri}")
    private String wsSubscriptionUri;

    @SpyBean
    private ClientMessageConsumer messageConsumer;

    @Autowired
    private WebSocketStompClient stompClient;

    @Autowired
    private ClientSubscriber clientSubscriber;

    private StompSession serverSession;

    @Before
    public void setUp() throws InterruptedException, ExecutionException, TimeoutException {
        serverSession = stompClient.connect(wsUrl, new StompSessionHandlerAdapter() {
        }).get(3, SECONDS);
    }

    @Test
    public void testClient() {
        await().atMost(5, SECONDS).until(clientSubscriber::isSubscribed);

        List<AlertMessage> messages = generateMessages();

        messages.forEach(this::sendMessage);

        await().atMost(5, SECONDS).until(() -> mockingDetails(messageConsumer).getInvocations().size() == messages.size());

        messages.forEach(this::verifyMessage);
    }

    private List<AlertMessage> generateMessages() {
        List<AlertMessage> messages = new ArrayList<>();
        messages.add(generateMessage("BTC-USD", new BigDecimal(300), new BigDecimal(400)));
        messages.add(generateMessage("BTC-EUR", new BigDecimal(1300), new BigDecimal(1400)));
        messages.add(generateMessage("BTC-USD", new BigDecimal(300), new BigDecimal(500)));
        return messages;
    }

    private void sendMessage(AlertMessage message) {
        serverSession.send(wsSubscriptionUri, message);
    }

    private void verifyMessage(AlertMessage message) {
        verify(messageConsumer).accept(eq(message));
    }

    private AlertMessage generateMessage(String pair, BigDecimal limit, BigDecimal lastValue) {
        AlertMessage message = new AlertMessage();
        message.setPair(pair);
        message.setLimit(limit);
        message.setLastValue(lastValue);
        message.setTimestamp(System.currentTimeMillis());
        return message;
    }

    @Configuration
    @EnableWebSocketMessageBroker
    static class ServerStubConfiguration implements WebSocketMessageBrokerConfigurer {

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/alerts");
            config.setApplicationDestinationPrefixes("/");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
            registry.addEndpoint("/alerts")
                    .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
                    .setAllowedOrigins("*").withSockJS();
        }
    }
}
