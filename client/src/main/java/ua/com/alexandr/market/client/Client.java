package ua.com.alexandr.market.client;

import java.math.BigDecimal;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public interface Client {
    void subscribe();

    void setAlert(String pair, BigDecimal limit);

    void removeAlert(String pair, BigDecimal limit);
}
