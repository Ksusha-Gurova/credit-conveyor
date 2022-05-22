package ru.neoflex.conveyor.exceptionHandler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorMessage {
    private String message;
}
