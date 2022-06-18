package ru.neoflex.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.client.DealClient;
import ru.neoflex.dossier.client.dto.ApplicationDTO;
import ru.neoflex.dossier.consumers.dto.EmailMessage;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService{
    private final ObjectMapper mapper = new ObjectMapper();
    private final EmailService emailService;
    private final DocumentService documentService;
    private final DealClient dealClient;

    @Override
    public void processFinishRegistrationEvent(String message) {
        log.debug("processFinishRegistrationEvent(), message = {}", message);

        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            emailService.sendSimpleEmail(emailMessage, createTextForFinishRegistration(emailMessage.getApplicationId()));
            log.info("processFinishRegistrationEvent(), отправляем письмо клиенту, тема: {}", emailMessage.getTheme());

        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение из топика (название топика), {}: {}", e.getClass(), e.getMessage());
        }
    }

    @Override
    public void processCreateDocumentsEvent(String message) {
        log.debug("processCreateDocumentsEvent(), message = {}", message);

        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            emailService.sendSimpleEmail(emailMessage, createTextForCreateDocuments(emailMessage.getApplicationId()));
            log.info("processCreateDocumentsEvent(), отправляем письмо клиенту, тема: {}", emailMessage.getTheme());

        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение, {}: {}", e.getClass(), e.getMessage());
        }
    }

    @Override
    public void processSendDocumentEvent(String message) {
        log.debug("processSendDocumentEvent(), message = {}", message);

        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            ApplicationDTO application = dealClient.getApplication(emailMessage.getApplicationId());
            log.debug("processSendDocumentEvent(), отправляем запрос в MC-Deal на получение данных о заявке, application = {}", application);

            File clientData = documentService.createClientDataDocument(application, emailMessage.getApplicationId());
            log.debug("processSendDocumentEvent(), сформировали clientData = {}", clientData);

            File creditData = documentService.createCreditDataDocument(application, emailMessage.getApplicationId());
            log.debug("processSendDocumentEvent(), сформировали creditData = {}", creditData);

            File paymentSchedule = documentService.createPaymentScheduleDocument(application, emailMessage.getApplicationId());
            log.debug("processSendDocumentEvent(), сформировали paymentSchedule = {}", paymentSchedule);

            emailService.sendEmailWithAttachment(
                    emailMessage,
                    createTextForSendDocument(emailMessage.getApplicationId()),
                    clientData, creditData, paymentSchedule);
            log.info("processSendDocumentEvent(), отправляем письмо клиенту, тема: {}", emailMessage.getTheme());

        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение, {}: {}", e.getClass(), e.getMessage());
        }
    }

    @Override
    public void processSendSesEvent(String message) {
        log.debug("processSendSesEvent(), message = {}", message);

        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            ApplicationDTO application = dealClient.getApplication(emailMessage.getApplicationId());
            log.debug("processSendDocumentEvent(), отправляем запрос в MC-Deal на получение данных о заявке, application = {}", application);

            emailService.sendSimpleEmail(emailMessage, createTextForSendSes(application.getSesCode()));
            log.info("processSendSesEvent(), отправляем письмо клиенту, тема: {}", emailMessage.getTheme());

        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение, {}: {}", e.getClass(), e.getMessage());
        }

    }

    @Override
    public void processCreditIssuedEvent(String message) {
        log.debug("processCreditIssuedEvent(), message = {}", message);

        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            emailService.sendSimpleEmail(emailMessage, createTextForCreditIssued());
            log.info("processCreditIssuedEvent(), отправляем письмо клиенту, тема: {}", emailMessage.getTheme());

        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение, {}: {}", e.getClass(), e.getMessage());
        }
    }

    @Override
    public void processApplicationDeniedEvent(String message) {
        log.debug("processApplicationDeniedEvent(), message = {}", message);

        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            emailService.sendSimpleEmail(emailMessage, createTextForApplicationDenied(emailMessage.getApplicationId()));
            log.info("processApplicationDeniedEvent(), отправляем письмо клиенту, тема: {}", emailMessage.getTheme());

        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение, {}: {}", e.getClass(), e.getMessage());
        }
    }

    public EmailMessage deserializeStringToEmailMessage(String message) throws JsonProcessingException {
        log.debug("deserializeStringToEmailMessage(), message = {}", message);

        try {
            EmailMessage emailMessage = mapper.readValue(message, EmailMessage.class);
            log.debug("deserializeStringToEmailMessage(), return emailMessage = {}", emailMessage);

            return emailMessage;
        } catch (Exception e) {
            log.error("deserializeStringToEmailMessage(), Ошибка, неправильный формат сообщения! message = {}", message);
            throw e;
        }
    }


    public String createTextForFinishRegistration(Long applicationId){
        log.debug("createTextForFinishRegistration(), applicationId = {}", applicationId);

        String messageText =
                "Добрый день!" +
                "\nВаша заявка №" + applicationId + " предварительно одобрена." +
                "\nПожалуйста перейдите по ссылке ниже для дальнейшего оформдения кредита:" +
                "\nhttp://localhost:8081/swagger-ui/index.html#/deal/calculateCredit";
        log.debug("createTextForFinishRegistration(), return messageText = {}", messageText);
        return messageText;
    }

    public String createTextForCreateDocuments(Long applicationId){
        log.debug("createTextForCreateDocuments(), applicationId = {}", applicationId);

        String messageText =
                "Добрый день!" +
                "\nВаша заявка №" + applicationId + " полностью одобрена." +
                "\nПерейдите по ссылке ниже для формирования документов:" +
                "\nhttp://localhost:8081/swagger-ui/index.html#/deal/sendDocuments";
        log.debug("createTextForCreateDocuments(), return messageText = {}", messageText);
        return messageText;
    }

    public String createTextForSendDocument(Long applicationId){
        log.debug("createTextForSendDocument(), applicationId = {}", applicationId);

        String messageText =
                "Добрый день!" +
                "\nВаши документы по заявке №" + applicationId + " во вложении." +
                "\nПерейдите по ссылке ниже для запроса на подписание документов:" +
                "\nhttp://localhost:8081/swagger-ui/index.html#/deal/signDocuments" +
                "\n" +
                "\nЕсли вы хотите отклонить заявку перейдите по следующей ссылке и укажите client_denied:" +
                "\nhttp://localhost:8081/swagger-ui/index.html#/deal/updateStatus";
        log.debug("createTextForSendDocument(), return messageText = {}", messageText);
        return messageText;
    }

    public String createTextForSendSes(String sesCode){
        log.debug("createTextForSendSes(), sesCode = {}", sesCode);

        String messageText =
                "Добрый день!" +
                "\nВаш ses-code: " + sesCode +
                "\nПройдите по ссылки ниже для завершения оформления кредита и введите ses-code" +
                "\nhttp://localhost:8081/swagger-ui/index.html#/deal/signDocumentsSesCode";
        log.debug("createTextForSendSes(), return messageText = {}", messageText);
        return messageText;
    }
    public String createTextForCreditIssued(){
        log.debug("createTextForCreditIssued()");

        String messageText =
                "Поздравляем!" +
                "\nВаш кредит успешно оформлен! В ближайшее время средства поступят на ваш счет";
        log.debug("createTextForCreditIssued(), return messageText = {}", messageText);
        return messageText;
    }

    public String createTextForApplicationDenied(Long applicationId){
        log.debug("createTextForApplicationDenied(), applicationId = {}", applicationId);

        String messageText =
                "Добрый день!" +
                "\nВаша заявка №" + applicationId + " отклонена";
        log.debug("createTextForApplicationDenied(), return messageText = {}", messageText);
        return messageText;
    }
}
