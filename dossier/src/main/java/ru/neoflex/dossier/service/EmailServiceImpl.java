package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.consumers.dto.EmailMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender emailSender;


    @Override
    public void sendSimpleEmail(EmailMessage emailMessage, String text) {
        log.info("sendSimpleEmail(), emailMessage = {}, text = {}", emailMessage, text);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setSubject(emailMessage.getTheme().getValue());
        simpleMailMessage.setText(text);
        emailSender.send(simpleMailMessage);
        log.info("sendSimpleEmail(), message successfully sended");

    }

    @Override
    public void sendEmailWithAttachment(EmailMessage emailMessage, String text) {

    }
}
