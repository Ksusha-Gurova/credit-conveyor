package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.consumers.dto.EmailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender emailSender;


    @Override
    public void sendSimpleEmail(EmailMessage emailMessage, String text) {
        log.debug("sendSimpleEmail(), emailMessage = {}, text = {}", emailMessage, text);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setSubject(emailMessage.getTheme().getValue());
        simpleMailMessage.setText(text);
        emailSender.send(simpleMailMessage);
        log.info("sendSimpleEmail(), сообщение успешно отправлено, тема: = {}", emailMessage.getTheme());

    }

    @Override
    public void sendEmailWithAttachment(EmailMessage emailMessage, String text, File clientData, File creditData, File paymentSchedule) throws MessagingException {
        log.debug("sendEmailWithAttachment(), emailMessage = {}, text = {}, File clientData, File creditData, File paymentSchedule", emailMessage, text);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(emailMessage.getAddress());
        messageHelper.setSubject(emailMessage.getAddress());
        messageHelper.setText(text);
        messageHelper.addAttachment("ClientData", clientData);
        messageHelper.addAttachment("CreditData", creditData);
        messageHelper.addAttachment("PaymentSchedule", paymentSchedule);
        emailSender.send(mimeMessage);
        log.info("sendEmailWithAttachment(), сообщение успешно отправлено, тема: = {}", emailMessage.getTheme());
    }
}
