package ru.neoflex.deal.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class DealExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorMessage handlerEntityNotFoundException (EntityNotFoundException e){
        return ErrorMessage.builder()
                .message(e.getMessage())
                .build();
    }
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorMessage handlerIllegalArgumentException (IllegalArgumentException e){
        return ErrorMessage.builder()
                .message(e.getMessage())
                .build();
    }
}
