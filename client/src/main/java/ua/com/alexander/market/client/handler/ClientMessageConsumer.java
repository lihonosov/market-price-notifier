package ua.com.alexander.market.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.com.alexander.market.client.model.AlertMessage;

import java.util.Date;
import java.util.function.Consumer;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class ClientMessageConsumer implements Consumer<AlertMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageConsumer.class);

    @Override
    public void accept(AlertMessage message) {
        LOGGER.debug("Received message: \tCurrency pair: {}\tLimit: {}\tLast value: {}\tTimestamp: {}",
                message.getPair(), message.getLimit(), message.getLastValue(), new Date(message.getTimestamp()));
    }
}
