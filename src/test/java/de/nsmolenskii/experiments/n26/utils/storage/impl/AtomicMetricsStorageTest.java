package de.nsmolenskii.experiments.n26.utils.storage.impl;

import de.nsmolenskii.experiments.n26.exceptions.InvalidTimestampException;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AtomicMetricsStorageTest {
    private static final long NOW = System.currentTimeMillis();
    private static final long ONE_SECOND = Duration.ofSeconds(5).toMillis();
    private static final long ONE_MINUTE = Duration.ofMinutes(1).toMillis();
    private static final BinaryOperator<Integer> SUM = (a, b) -> a + b;

    private AtomicMetricsStorage<Integer> storage;

    @Before
    public void setUp() {
        storage = new AtomicMetricsStorage<>(ChronoUnit.MINUTES, ChronoUnit.SECONDS, 90, () -> 0, () -> NOW);
    }

    @Test
    public void shouldAcceptSingleValue() {
        storage.update(NOW, plus(5));

        final Integer value = storage.getReference(NOW).getValue();

        assertThat(value, is(5));
    }

    @Test
    public void shouldAcceptMultipleValues() {
        storage.update(NOW, plus(5));
        storage.update(NOW, plus(10));
        storage.update(NOW, plus(20));

        final Integer value = storage.getReference(NOW).getValue();

        assertThat(value, is(35));
    }

    @Test(expected = InvalidTimestampException.class)
    public void shouldRejectOldValue() {
        storage.update(NOW - ONE_MINUTE - ONE_SECOND, plus(5));
    }

    @Test(expected = InvalidTimestampException.class)
    public void shouldRejectFutureValue() {
        storage.update(NOW + ONE_SECOND, plus(5));
    }

    @Test
    public void shouldReduceEmptyValues() {
        final Integer value = storage.reduce(SUM);

        assertThat(value, is(0));
    }

    @Test
    public void shouldReduceSingleValue() {
        storage.update(NOW, plus(5));

        final Integer value = storage.reduce(SUM);

        assertThat(value, is(5));
    }

    @Test
    public void shouldReduceMultipleValues() {
        storage.update(NOW - ONE_MINUTE, plus(5));
        storage.update(NOW - ONE_SECOND, plus(10));
        storage.update(NOW, plus(20));

        final Integer value = storage.reduce(SUM);

        assertThat(value, is(35));
    }

    @Test
    public void shouldReduceGroupedValues() {
        storage.update(NOW, plus(5));
        storage.update(NOW, plus(10));
        storage.update(NOW, plus(20));

        final Integer value = storage.reduce(SUM);

        assertThat(value, is(35));
    }

    private UnaryOperator<Integer> plus(int i) {
        return value -> value + i;
    }
}