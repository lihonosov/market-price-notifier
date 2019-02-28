package ua.com.alexander.market.server.services.subscriptions;

import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public interface ClientSubscriptionService {
    void subscribe(CurrencyPair pair, BigDecimal limit);

    void unsubscribe(CurrencyPair pair, BigDecimal limit);
}
