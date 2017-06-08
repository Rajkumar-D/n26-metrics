package de.nsmolenskii.experiments.n26.domains;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.n26.domains.TransactionStatisticBuilder.ImmutableTransactionStatistic;
import de.nsmolenskii.experiments.n26.utils.json.Json;
import org.immutables.value.Value;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableTransactionStatistic.class)
@JsonDeserialize(as = ImmutableTransactionStatistic.class)
public interface TransactionStatistic {

    long getCount();

    double getSum();

    double getMax();

    double getMin();

    @Value.Derived
    default double getAvg() {
        return getCount() > 0 ? getSum() / getCount() : Double.NaN;
    }

    TransactionStatistic ZERO = TransactionStatistic.builder()
            .count(0)
            .sum(0.0)
            .max(Double.NaN)
            .min(Double.NaN)
            .build();

    static TransactionStatisticBuilder builder() {
        return new TransactionStatisticBuilder();
    }

    default TransactionStatistic register(double amount) {
        return merge(TransactionStatistic.builder()
                .count(1)
                .sum(amount)
                .min(amount)
                .max(amount)
                .build());
    }

    default TransactionStatistic merge(TransactionStatistic that) {
        if (this.equals(ZERO)) {
            return that;
        }
        if (that.equals(ZERO)) {
            return this;
        }
        return TransactionStatistic.builder()
                .count(this.getCount() + that.getCount())
                .sum(this.getSum() + that.getSum())
                .min(Math.min(this.getMin(), that.getMin()))
                .max(Math.max(this.getMax(), that.getMax()))
                .build();
    }
}
