package de.nsmolenskii.experiments.n26.controllers;

import de.nsmolenskii.experiments.n26.exceptions.InvalidTimestampException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class DefaultControllerAdvice {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException() {
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(InvalidTimestampException.class)
    public void handleInvalidTimestampException() {
    }
}
