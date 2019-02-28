package ua.com.alexandr.market.client.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SuccessCallback;
import ua.com.alexandr.market.client.ClientApplicationProperties;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class ClientSubscriber implements SuccessCallback<StompSession>, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSubscriber.class);

    private StompSession.Subscription subscription;
    private final ClientApplicationProperties applicationProperties;
    private final StompSessionHandler sessionHandler;

    @Autowired
    public ClientSubscriber(ClientApplicationProperties applicationProperties, StompSessionHandler sessionHandler) {
        this.applicationProperties = applicationProperties;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void onSuccess(StompSession session) {
        subscription = session.subscribe(applicationProperties.getWsSubscriptionUri(), sessionHandler);
        LOGGER.debug("[{}] Subscribed", applicationProperties.getWsUrl());
    }

    public boolean isSubscribed() {
        return subscription != null;
    }

    @Override
    public void close() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
