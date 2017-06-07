package de.nsmolenskii.experiments.n26.utils.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static de.nsmolenskii.experiments.n26.utils.validation.WithinLastLongValidator.NOW;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WithinLastLongValidatorTest {

    @Mock
    private WithinLast annotation;

    @Mock
    private ConstraintValidatorContext context;

    private WithinLastLongValidator validator;

    @Before
    public void setUp() {
        NOW = System::currentTimeMillis;

        when(annotation.duration()).thenReturn(1);
        when(annotation.unit()).thenReturn(SECONDS);

        validator = new WithinLastLongValidator();
        validator.initialize(annotation);
    }

    @Test
    public void shouldAcceptValidValue() {
        final long value = System.currentTimeMillis() - 500;

        final boolean valid = validator.isValid(value, context);

        assertThat(valid).isTrue();
    }

    @Test
    public void shouldRejectInvalidValue() {
        final long value = System.currentTimeMillis() - 1500;

        final boolean valid = validator.isValid(value, context);

        assertThat(valid).isFalse();
    }
}