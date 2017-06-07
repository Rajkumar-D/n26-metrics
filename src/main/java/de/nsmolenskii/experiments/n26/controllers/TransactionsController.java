package de.nsmolenskii.experiments.n26.controllers;

import de.nsmolenskii.experiments.n26.domains.Transaction;
import de.nsmolenskii.experiments.n26.services.MetricsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final MetricsService metricsService;

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public void registerTransaction(@Valid @RequestBody final Transaction transaction) {
        metricsService.register(transaction);
    }

}
