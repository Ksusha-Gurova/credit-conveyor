package ru.neoflex.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.DocumentApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.service.GatewayDocumentService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GatewayDocumentController implements DocumentApi {

    private final GatewayDocumentService service;

    @Override
    public ResponseEntity<Void> createDocumentRequest(Long applicationId) {
        service.createDocumentRequest(applicationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signDocumentsRequest(Long applicationId, String statusClientDenied) {
        service.signDocumentRequest(applicationId, statusClientDenied);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> verifySesCodeRequest(Long applicationId, String ses) {
        service.verifySesCodeRequest(applicationId, ses);
        return ResponseEntity.ok().build();
    }
}
