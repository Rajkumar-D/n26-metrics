package de.nsmolenskii.experiments.n26.services.impl;

import de.nsmolenskii.experiments.n26.domains.Transaction;
import de.nsmolenskii.experiments.n26.domains.TransactionStatistic;
import de.nsmolenskii.experiments.n26.utils.storage.MetricsStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class MetricsServiceImplTest {

    private static final Long TIMESTAMP = new Random().nextLong();
    private static final double AMOUNT = new Random().nextDouble();

    @Mock
    private MetricsStorage<TransactionStatistic> storage;

    @InjectMocks
    private MetricsServiceImpl service;

    @Mock
    private TransactionStatistic statistic;

    @Mock
    private TransactionStatistic anotherStatistic;

    @Mock
    private TransactionStatistic reducedStatistic;


    @Captor
    private ArgumentCaptor<UnaryOperator<TransactionStatistic>> unaryOperatorArgumentCaptor;

    @Captor
    private ArgumentCaptor<BinaryOperator<TransactionStatistic>> binaryOperatorArgumentCaptor;

    @Test
    public void shouldRegisterTransaction() {
        service.register(Transaction.of(AMOUNT, TIMESTAMP));
        verify(storage).update(eq(TIMESTAMP), unaryOperatorArgumentCaptor.capture());

        unaryOperatorArgumentCaptor.getValue().apply(statistic);
        verify(statistic).register(AMOUNT);
    }

    @Test
    public void shouldReturnTransactionStatistics() {
        doReturn(reducedStatistic).when(storage).reduce(any());

        final TransactionStatistic actual = service.getTransactionStatistics();

        assertThat(actual, is(reducedStatistic));
        verify(storage).reduce(binaryOperatorArgumentCaptor.capture());

        binaryOperatorArgumentCaptor.getValue().apply(statistic, anotherStatistic);
        verify(statistic).merge(anotherStatistic);
    }
}