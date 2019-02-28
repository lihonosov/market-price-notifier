package ua.com.alexander.market.server.services.tickers;

import io.reactivex.functions.Consumer;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.alexander.market.server.model.AlertMessage;
import ua.com.alexander.market.server.services.notifications.ClientsNotificationService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class TickersConsumer implements Consumer<Ticker> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TickersConsumer.class);

    private final ClientsNotificationService clientsNotificationService;

    private final Map<CurrencyPair, BigDecimal> subscriptionLimits = new ConcurrentHashMap<>();

    @Autowired
    public TickersConsumer(ClientsNotificationService clientsNotificationService) {
        this.clientsNotificationService = clientsNotificationService;
    }

    @Override
    public void accept(Ticker ticker) {
        CurrencyPair currencyPair = ticker.getCurrencyPair();
        BigDecimal limit = subscriptionLimits.get(currencyPair);
        LOGGER.debug("[{}] Incoming ticker: {} vs {}", currencyPair, limit, ticker.getLast());
        if (limit != null && limit.compareTo(ticker.getLast()) < 0) {
            LOGGER.debug("Incoming ticker for currency pair {} is greater than the limit {}, current: {}, sending notification...", currencyPair, limit, ticker.getLast());
            clientsNotificationService.notify(new AlertMessage(currencyPair, limit, ticker.getLast(), ticker.getTimestamp().getTime()));
        }
    }

    public void setLimit(CurrencyPair pair, BigDecimal limit) {
        subscriptionLimits.put(pair, limit);
    }

    public boolean removeLimit(CurrencyPair pair, BigDecimal limit) {
        return subscriptionLimits.remove(pair, limit);
    }
}
