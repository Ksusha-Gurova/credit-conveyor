package ru.neoflex.dossier.service;

import ru.neoflex.dossier.consumers.dto.EmailMessage;

public interface DossierService {
    void processFinishRegistrationEvent(String emailMessage);
}
