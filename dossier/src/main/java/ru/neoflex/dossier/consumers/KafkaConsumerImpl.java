package ru.neoflex.dossier.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.consumers.dto.EmailMessage;
import ru.neoflex.dossier.service.DossierService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer{

    @Value("${kafka.topics.finish-registration}") private static String topicFinishRegistration;
    @Value("${kafka.topics.create-documents}") private String topicCreateDocuments;
    @Value("${kafka.topics.send-documents}") private String topicSendDocuments;
    @Value("${kafka.topics.send-ses}") private String topicSendSes;
    @Value("${kafka.topics.credit-issued}") private String topicCreditIssued;
    @Value("${kafka.topics.application-denied}") private String topicApplicationDenied;

    private final DossierService service;

    @Override
    @KafkaListener(topics = "${kafka.topics.finish-registration}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFinishRegistrationEvent(String emailMessage) {
        log.info("consumeFinishRegistrationEvent(), emailMessage = {}", emailMessage);
        service.processFinishRegistrationEvent(emailMessage);
    }
}
