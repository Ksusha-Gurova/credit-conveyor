package ru.neoflex.deal.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.client.dto.EmailMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaClientImpl implements KafkaClient{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topicName, EmailMessage emailMessage) {
        log.info("send(), message send to topic: {}, messageDto = {}", topicName, emailMessage);
        kafkaTemplate.send(topicName, emailMessage);
    }
}
