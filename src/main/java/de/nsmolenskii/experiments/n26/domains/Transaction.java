package de.nsmolenskii.experiments.n26.domains;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nsmolenskii.experiments.n26.domains.TransactionBuilder.ImmutableTransaction;
import de.nsmolenskii.experiments.n26.utils.json.Json;
import de.nsmolenskii.experiments.n26.utils.validation.WithinLast;
import org.immutables.value.Value;
import org.immutables.value.Value.Parameter;

import javax.validation.constraints.Min;

import static java.time.temporal.ChronoUnit.SECONDS;

@Json
@Value.Immutable
@JsonSerialize(as = ImmutableTransaction.class)
@JsonDeserialize(as = ImmutableTransaction.class)
public interface Transaction {

    @Min(0)
    @Parameter
    double getAmount();

    @Parameter
    @WithinLast(duration = 60, unit = SECONDS)
    long getTimestamp();

    static Transaction of(final double amount, final long timestamp) {
        return ImmutableTransaction.of(amount, timestamp);
    }

}
