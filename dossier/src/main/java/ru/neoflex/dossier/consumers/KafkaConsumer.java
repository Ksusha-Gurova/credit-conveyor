package ru.neoflex.dossier.consumers;

import ru.neoflex.dossier.consumers.dto.EmailMessage;

public interface KafkaConsumer {
    void consumeFinishRegistrationEvent(String emailMessage);
}
