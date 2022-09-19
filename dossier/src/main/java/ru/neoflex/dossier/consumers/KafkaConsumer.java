package ru.neoflex.dossier.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.service.DossierService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final DossierService service;

    @KafkaListener(topics = "${kafka.topics.finish-registration}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFinishRegistrationEvent(String emailMessage) {
        log.info("consumeFinishRegistrationEvent(), emailMessage = {}", emailMessage);
        service.processFinishRegistrationEvent(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topics.create-documents}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCreateDocumentsEvent(String emailMessage) {
        log.info("consumeCreateDocumentsEvent(), emailMessage = {}", emailMessage);
        service.processCreateDocumentsEvent(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topics.send-documents}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSendDocumentsEvent(String emailMessage) {
        log.info("consumeSendDocumentsEvent(), emailMessage = {}", emailMessage);
        service.processSendDocumentEvent(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topics.send-ses}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSendSesEvent(String emailMessage) {
        log.info("consumeSendSesEvent(), emailMessage = {}", emailMessage);
        service.processSendSesEvent(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topics.credit-issued}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCreditIssuedEvent(String emailMessage) {
        log.info("consumeCreditIssuedEvent(), emailMessage = {}", emailMessage);
        service.processCreditIssuedEvent(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topics.application-denied}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeApplicationDeniedEvent(String emailMessage) {
        log.info("consumeApplicationDeniedEvent(), emailMessage = {}", emailMessage);
        service.processApplicationDeniedEvent(emailMessage);
    }
}
