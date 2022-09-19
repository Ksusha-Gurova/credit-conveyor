package ru.neoflex.gateway.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GatewayExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Errormessage handlerIllegalArgumentException (IllegalArgumentException e){
        return Errormessage.builder()
                .message(e.getMessage())
                .build();
    }
}
