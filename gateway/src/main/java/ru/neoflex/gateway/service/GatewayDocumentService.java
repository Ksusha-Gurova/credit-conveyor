package ru.neoflex.gateway.service;

public interface GatewayDocumentService {
    void createDocumentRequest(Long applicationId);

    void signDocumentRequest(Long applicationId, String statusClientDenied);

    void verifySesCodeRequest(Long applicationId, String ses);
}
