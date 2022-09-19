package ru.neoflex.gateway.service;

public interface GatewayDocumentService {
    void createDocumentRequest(Long applicationId);

    void signDocumentRequest(Long applicationId);

    void verifySesCodeRequest(Long applicationId, String ses);

    void denyOnApplication(Long applicationId);
}
