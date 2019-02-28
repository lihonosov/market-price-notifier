package ua.com.alexandr.market.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import ua.com.alexandr.market.client.model.AlertMessage;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class WebSocketSessionHandler extends StompSessionHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketSessionHandler.class);

    private final Consumer<AlertMessage> messageConsumer;

    @Autowired
    public WebSocketSessionHandler(Consumer<AlertMessage> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return AlertMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (payload instanceof AlertMessage) {
            messageConsumer.accept((AlertMessage) payload);
        } else {
            LOGGER.warn("Received unknown message: {}", payload);
        }
    }
}
