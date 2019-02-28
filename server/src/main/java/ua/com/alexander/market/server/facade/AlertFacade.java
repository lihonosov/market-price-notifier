package ua.com.alexander.market.server.facade;

import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ua.com.alexander.market.server.model.AlertInput;
import ua.com.alexander.market.server.services.subscriptions.ClientSubscriptionService;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Component
public class AlertFacade {
    private final ClientSubscriptionService clientSubscriber;
    private final ConversionService conversionService;

    @Autowired
    public AlertFacade(ClientSubscriptionService clientSubscriber, ConversionService mvcConversionService) {
        this.clientSubscriber = clientSubscriber;
        this.conversionService = mvcConversionService;
    }

    public void subscribe(AlertInput input) {
        clientSubscriber.subscribe(convertCurrencyPair(input), input.getLimit());
    }

    public void unsubscribe(AlertInput input) {
        clientSubscriber.unsubscribe(convertCurrencyPair(input), input.getLimit());
    }

    private CurrencyPair convertCurrencyPair(AlertInput input) {
        return conversionService.convert(input.getPair(), CurrencyPair.class);
    }
}
