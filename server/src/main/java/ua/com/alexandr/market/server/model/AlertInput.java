package ua.com.alexandr.market.server.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Oleksandr Lykhonosov on 2019-02-24.
 * lihonosov@gmail.com
 */
public class AlertInput {
    @NotEmpty(message = "Currency Pair is required")
    private String pair;
    @NotNull(message = "Limit is required")
    private BigDecimal limit;

    public AlertInput(String pair, BigDecimal limit) {
        this.pair = pair;
        this.limit = limit;
    }

    public String getPair() {
        return pair;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertInput that = (AlertInput) o;
        return Objects.equals(pair, that.pair) &&
                Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, limit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pair", pair)
                .append("limit", limit)
                .toString();
    }
}
