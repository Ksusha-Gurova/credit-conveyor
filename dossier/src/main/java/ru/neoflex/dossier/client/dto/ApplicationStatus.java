package ru.neoflex.dossier.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicationStatus {
    PREAPPROVAL("PREAPPROVAL"),
    APPROVED("APPROVED"),
    CC_DENIED("CC_DENIED"),
    CC_APPROVED("CC_APPROVED"),
    PREPARE_DOCUMENTS("PREPARE_DOCUMENTS"),
    DOCUMENT_CREATED("DOCUMENT_CREATED"),
    CLIENT_DENIED("CLIENT_DENIED"),
    DOCUMENT_SIGNED("DOCUMENT_SIGNED"),
    CREDIT_ISSUED("CREDIT_ISSUED");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ApplicationStatus fromValue(String value) {
        for (ApplicationStatus b : ApplicationStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
