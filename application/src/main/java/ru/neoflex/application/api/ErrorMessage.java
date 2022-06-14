package ru.neoflex.application.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorMessage {
    private String message;
}
