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

    double getAvg();

    double getMax();

    double getMin();

    TransactionStatistic ZERO = TransactionStatistic.builder()
            .count(0)
            .sum(0.0)
            .avg(Double.NaN)
            .max(Double.NaN)
            .min(Double.NaN)
            .build();

    static TransactionStatisticBuilder builder() {
        return new TransactionStatisticBuilder();
    }


}
