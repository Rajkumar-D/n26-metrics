package de.nsmolenskii.experiments.n26.utils.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import de.nsmolenskii.experiments.n26.exceptions.InvalidTimestampException;
import de.nsmolenskii.experiments.n26.utils.storage.MetricsStorage;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

public class AtomicMetricsStorage<T> implements MetricsStorage<T> {

    private final Supplier<Long> now;
    private final Supplier<T> factory;

    private final TemporalUnit targetUnit;
    private final TemporalUnit groupingUnit;

    private final AtomicReferenceArray<Reference<T>> store;

    public static <T> MetricsStorage<T> lastMinute(final Supplier<T> factory) {
        return new AtomicMetricsStorage<>(MINUTES, SECONDS, 90, factory, System::currentTimeMillis);
    }

    @VisibleForTesting
    protected AtomicMetricsStorage(
            final TemporalUnit targetUnit,
            final TemporalUnit groupingUnit,
            final int bufferSize,
            final Supplier<T> factory,
            final Supplier<Long> now
    ) {
        this.now = now;
        this.factory = factory;
        this.targetUnit = targetUnit;
        this.groupingUnit = groupingUnit;
        this.store = new AtomicReferenceArray<>(bufferSize);
    }

    @Override
    public void update(final long timestamp, final UnaryOperator<T> updater) {
        getReference(timestamp).update(updater);
    }

    @Override
    public T reduce(final BinaryOperator<T> reducer) {
        return getReferenceStream().reduce(factory.get(), reducer);
    }

    @VisibleForTesting
    protected Reference<T> getReference(final long timestamp) {
        final int index = checkedIndexFor(timestamp);
        final int offset = offset(index);
        return store.updateAndGet(offset, value -> actual(index, value));
    }

    private Stream<T> getReferenceStream() {
        final long now = this.now.get();

        final int firstIndex = minimalIndexFor(now);
        final int lastIndex = currentIndexFor(now);

        return IntStream.rangeClosed(firstIndex, lastIndex)
                .mapToObj(index -> historical(index, store.get(offset(index))))
                .filter(Objects::nonNull)
                .map(Reference::getValue);
    }

    @Nullable
    private Reference<T> historical(final int index, @Nullable final Reference<T> reference) {
        return reference != null && reference.getIndex() == index ? reference : null;
    }

    private Reference<T> actual(final int index, @Nullable final Reference<T> value) {
        return value == null || value.getIndex() < index ? new Reference<>(index, factory.get()) : value;
    }

    private int offset(final int index) {
        return index % store.length();
    }

    private int currentIndexFor(final long timestamp) {
        return (int) Duration.of(timestamp, MILLIS).get(groupingUnit);
    }

    private int minimalIndexFor(final long timestamp) {
        return (int) Duration.of(timestamp, MILLIS).minus(1, targetUnit).get(groupingUnit);
    }

    private int checkedIndexFor(long timestamp) {
        long now = this.now.get();

        int minimalIndex = minimalIndexFor(now);
        int maximalIndex = currentIndexFor(now);

        int index = currentIndexFor(timestamp);

        InvalidTimestampException.check(index >= minimalIndex, "Timestamp is too old");
        InvalidTimestampException.check(index <= maximalIndex, "Timestamp is too young");

        return index;
    }

    @VisibleForTesting
    protected static class Reference<E> {
        private final long index;
        private final AtomicReference<E> value;

        public Reference(long index, E value) {
            this.index = index;
            this.value = new AtomicReference<>(value);
        }

        public void update(UnaryOperator<E> updater) {
            value.updateAndGet(updater);
        }

        public E getValue() {
            return value.get();
        }

        public long getIndex() {
            return index;
        }
    }
}
