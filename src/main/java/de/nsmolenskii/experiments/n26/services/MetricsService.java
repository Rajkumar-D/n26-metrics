package de.nsmolenskii.experiments.n26.services;

import de.nsmolenskii.experiments.n26.domains.Transaction;

public interface MetricsService {
    void register(Transaction transaction);
}
