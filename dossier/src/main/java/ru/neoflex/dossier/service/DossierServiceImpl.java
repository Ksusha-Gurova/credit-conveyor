package ru.neoflex.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.consumers.dto.EmailMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService{
    private final ObjectMapper mapper = new ObjectMapper();
    private final EmailService emailService;

    @Override

    public void processFinishRegistrationEvent(String message) {
        log.info("processFinishRegistrationEvent(), emailMessage = {}", message);
        try {
            EmailMessage emailMessage = deserializeStringToEmailMessage(message);
            emailService.sendSimpleEmail(emailMessage,
                    "Добрый день! " +
                    "\n" +
                    "\nВаша заявка предварительно одобрена. Пожалуйста перейдите по ссылке ниже " +
                    "\n");
        } catch (Exception e) {
            log.error("Ошибка при попытке обработать сообщение, {}: {}", e.getClass(), e.getMessage());
        }
    }

    public EmailMessage deserializeStringToEmailMessage(String message) throws JsonProcessingException {
        try {
            return mapper.readValue(message, EmailMessage.class);
        } catch (Exception e) {
            log.error("deserializeStringToEmailMessage(), Ошибка, неправильный формат сообщения! message = {}", message);
            throw e;
        }
    }
}
