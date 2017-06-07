package de.nsmolenskii.experiments.n26.utils.validation;

import com.google.common.annotations.VisibleForTesting;

import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.util.function.Supplier;

public class WithinLastLongValidator implements ConstraintValidator<WithinLast, Long> {

    @VisibleForTesting
    public static Supplier<Long> NOW = System::currentTimeMillis;

    private Duration duration;

    @Override
    public void initialize(final WithinLast annotation) {
        duration = Duration.of(annotation.duration(), annotation.unit());
    }

    @Override
    public boolean isValid(@Nullable final Long value, final ConstraintValidatorContext context) {
        return value == null || NOW.get() - value <= duration.toMillis();
    }
}
