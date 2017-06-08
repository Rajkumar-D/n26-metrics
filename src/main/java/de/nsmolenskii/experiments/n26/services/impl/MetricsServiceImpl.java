package de.nsmolenskii.experiments.n26.services.impl;

import de.nsmolenskii.experiments.n26.domains.Transaction;
import de.nsmolenskii.experiments.n26.domains.TransactionStatistic;
import de.nsmolenskii.experiments.n26.services.MetricsService;
import de.nsmolenskii.experiments.n26.utils.storage.impl.AtomicMetricsStorage;
import de.nsmolenskii.experiments.n26.utils.storage.MetricsStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.nsmolenskii.experiments.n26.domains.TransactionStatistic.ZERO;

@Service
@AllArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MetricsStorage<TransactionStatistic> transactions;

    @Autowired
    public MetricsServiceImpl() {
        this(AtomicMetricsStorage.lastMinute(() -> ZERO));
    }

    @Override
    public void register(final Transaction transaction) {
        transactions.update(transaction.getTimestamp(), statistic -> statistic.register(transaction.getAmount()));
    }

    @Override
    public TransactionStatistic getTransactionStatistics() {
        return transactions.reduce(TransactionStatistic::merge);
    }

}
