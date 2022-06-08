package ru.neoflex.deal.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ErrorMessage handlerEntityNotFoundException (EntityNotFoundException e){
        return ErrorMessage.builder()
                .message(e.getMessage())
                .build();
    }
}
