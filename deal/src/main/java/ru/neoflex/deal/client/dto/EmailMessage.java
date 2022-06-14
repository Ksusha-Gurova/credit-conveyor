package ru.neoflex.deal.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Getter
public class EmailMessage {
    private String address;
    private ThemeEnum theme;
    private Long applicationId;

    @Getter
    @AllArgsConstructor
    @ToString
    public enum ThemeEnum {
        FINISH_REGISTRATION("Finish-registration"),
        CREATE_DOCUMENTS("Create-documents"),
        SEND_DOCUMENTS("send-documents"),
        SEND_SES("send-ses"),
        CREDIT_ISSUED("credit-issued"),
        APPLICATION_DENIED("application-denied");

        private final String value;
    }
}
