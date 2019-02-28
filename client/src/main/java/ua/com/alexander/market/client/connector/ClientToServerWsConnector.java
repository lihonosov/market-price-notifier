package ua.com.alexander.market.client.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ua.com.alexander.market.client.ClientApplicationProperties;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class ClientToServerWsConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientToServerWsConnector.class);

    private final ClientApplicationProperties applicationProperties;
    private final StompSessionHandler sessionHandler;
    private final WebSocketStompClient stompClient;
    private final ClientSubscriber clientSuccessCallback;

    @Autowired
    public ClientToServerWsConnector(WebSocketStompClient stompClient, StompSessionHandler sessionHandler,
                                     ClientApplicationProperties applicationProperties, ClientSubscriber clientSuccessCallback) {
        this.stompClient = stompClient;
        this.sessionHandler = sessionHandler;
        this.applicationProperties = applicationProperties;
        this.clientSuccessCallback = clientSuccessCallback;
    }

    public void connect() {
        LOGGER.debug("Connecting to {} ...", applicationProperties.getWsUrl());
        ListenableFuture<StompSession> session = stompClient.connect(applicationProperties.getWsUrl(), sessionHandler);
        session.addCallback(clientSuccessCallback, ex -> LOGGER.error(ex.getMessage()));
    }
}
