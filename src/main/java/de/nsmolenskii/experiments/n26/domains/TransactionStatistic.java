package de.nsmolenskii.experiments.n26.domains;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.n26.utils.json.JsonSnakeCase;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSnakeCase
@JsonSerialize(as = ImmutableTransactionStatistic.class)
@JsonDeserialize(as = ImmutableTransactionStatistic.class)
public interface TransactionStatistic {

    long getCount();

    double getSum();

    double getAvg();

    double getMax();

    double getMin();

    TransactionStatistic ZERO = ImmutableTransactionStatistic.builder()
            .count(0)
            .sum(0.0)
            .avg(Double.NaN)
            .max(Double.NaN)
            .min(Double.NaN)
            .build();



}
