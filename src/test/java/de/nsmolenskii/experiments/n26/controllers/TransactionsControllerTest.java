package de.nsmolenskii.experiments.n26.controllers;

import de.nsmolenskii.experiments.n26.domains.Transaction;
import de.nsmolenskii.experiments.n26.exceptions.InvalidTimestampException;
import de.nsmolenskii.experiments.n26.services.MetricsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.nsmolenskii.experiments.n26.utils.validation.WithinLastLongValidator.NOW;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionsController.class)
public class TransactionsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MetricsService metricsService;


    @Before
    public void setUp() {
        NOW = () -> 1478192204000L;
    }

    @Test
    public void shouldAcceptValidRequest() throws Exception {
        mvc.perform(post("/api/transactions")
                .contentType("application/json")
                //language=JSON
                .content("{\"amount\": 12.3,\"timestamp\": 1478192204000}"))
                .andExpect(status().isCreated())
                .andExpect(content().bytes(new byte[0]));

        verify(metricsService).register(Transaction.of(12.3, 1478192204000L));
    }

    @Test
    public void shouldValidateRequest() throws Exception {
        mvc.perform(post("/api/transactions")
                .contentType("application/json")
                //language=JSON
                .content("{}"))
                .andExpect(status().isNoContent())
                .andExpect(content().bytes(new byte[0]));

        verifyZeroInteractions(metricsService);
    }

    @Test
    public void shouldHandleInvalidTimestampException() throws Exception {
        doThrow(new InvalidTimestampException(""))
                .when(metricsService).register(any(Transaction.class));

        mvc.perform(post("/api/transactions")
                .contentType("application/json")
                //language=JSON
                .content("{\"amount\": 12.3,\"timestamp\": 1478192204000}"))
                .andExpect(status().isNoContent())
                .andExpect(content().bytes(new byte[0]));
    }
}