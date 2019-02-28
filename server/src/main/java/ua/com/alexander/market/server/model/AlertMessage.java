package ua.com.alexander.market.server.model;

import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class AlertMessage {
    private final CurrencyPair pair;
    private final BigDecimal limit;
    private final BigDecimal lastValue;
    private final long timestamp;

    public AlertMessage(CurrencyPair pair, BigDecimal limit, BigDecimal lastValue, long timestamp) {
        this.pair = pair;
        this.limit = limit;
        this.lastValue = lastValue;
        this.timestamp = timestamp;
    }

    public CurrencyPair getPair() {
        return pair;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getLastValue() {
        return lastValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertMessage that = (AlertMessage) o;
        return timestamp == that.timestamp &&
                Objects.equals(pair, that.pair) &&
                Objects.equals(limit, that.limit) &&
                Objects.equals(lastValue, that.lastValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, limit, lastValue, timestamp);
    }
}
