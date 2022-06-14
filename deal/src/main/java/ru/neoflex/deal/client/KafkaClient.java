package ru.neoflex.deal.client;

import ru.neoflex.deal.client.dto.EmailMessage;

public interface KafkaClient {
    void send (String topicName, EmailMessage emailMessage);
}
