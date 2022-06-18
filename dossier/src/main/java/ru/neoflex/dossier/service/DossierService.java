package ru.neoflex.dossier.service;

import ru.neoflex.dossier.consumers.dto.EmailMessage;

public interface DossierService {
    void processFinishRegistrationEvent(String emailMessage);

    void processCreateDocumentsEvent(String emailMessage);

    void processSendDocumentEvent(String emailMessage);

    void processSendSesEvent(String emailMessage);

    void processCreditIssuedEvent(String emailMessage);

    void processApplicationDeniedEvent(String emailMessage);
}
