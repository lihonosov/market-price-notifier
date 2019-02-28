package ua.com.alexander.market.client.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
@Data
public class AlertMessage {
    private String pair;
    private BigDecimal limit;
    private BigDecimal lastValue;
    private long timestamp;
}
