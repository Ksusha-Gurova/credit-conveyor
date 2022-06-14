package ru.neoflex.dossier.service;

import ru.neoflex.dossier.consumers.dto.EmailMessage;

public interface EmailService {

    void sendSimpleEmail(EmailMessage emailMessage, String text);

    void sendEmailWithAttachment(EmailMessage emailMessage, String text);
}
