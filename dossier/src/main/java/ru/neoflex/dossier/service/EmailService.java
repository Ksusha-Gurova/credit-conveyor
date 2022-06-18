package ru.neoflex.dossier.service;

import ru.neoflex.dossier.consumers.dto.EmailMessage;

import javax.mail.MessagingException;
import java.io.File;

public interface EmailService {

    void sendSimpleEmail(EmailMessage emailMessage, String text);

    void sendEmailWithAttachment(
            EmailMessage emailMessage,
            String text,
            File clientData,
            File creditData,
            File paymentSchedule) throws MessagingException;
}
