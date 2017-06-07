package de.nsmolenskii.experiments.n26.services.impl;

import de.nsmolenskii.experiments.n26.domains.Transaction;
import de.nsmolenskii.experiments.n26.domains.TransactionStatistic;
import de.nsmolenskii.experiments.n26.services.MetricsService;
import org.springframework.stereotype.Service;

@Service
public class MetricsServiceImpl implements MetricsService {

    @Override
    public void register(final Transaction transaction) {
        throw new UnsupportedOperationException(); //TODO
    }

    @Override
    public TransactionStatistic getTransactionStatistics() {
        throw new UnsupportedOperationException(); //TODO
    }

}
