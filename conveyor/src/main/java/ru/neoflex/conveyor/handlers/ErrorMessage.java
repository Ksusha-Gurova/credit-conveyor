package ru.neoflex.conveyor.handlers;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorMessage {
    private String message;
}
