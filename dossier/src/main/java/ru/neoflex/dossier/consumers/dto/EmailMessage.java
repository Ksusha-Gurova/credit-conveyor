package ru.neoflex.dossier.consumers.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailMessage {
    private String address;
    private ThemeEnum theme;
    private Long applicationId;

    @Getter
    @AllArgsConstructor
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
