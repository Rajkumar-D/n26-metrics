package de.nsmolenskii.experiments.n26.controllers;

import de.nsmolenskii.experiments.n26.exceptions.InvalidTimestampException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ResponseStatus(NO_CONTENT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(){
    }

    @ResponseStatus(NO_CONTENT)
    @ExceptionHandler(InvalidTimestampException.class)
    public void handleInvalidTimestampException() {
    }

}
