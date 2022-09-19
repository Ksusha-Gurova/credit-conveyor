package ru.neoflex.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AdminApi;
import org.openapitools.model.ApplicationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.service.GatewayAdminService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GatewayAdminController implements AdminApi {

    private final GatewayAdminService service;

    @Override
    public ResponseEntity<List<ApplicationDTO>> getAllApplication() {
        log.info("getAllApplication()");
        return ResponseEntity.ok(service.getAllApplication());
    }

    @Override
    public ResponseEntity<ApplicationDTO> getApplication(Long applicationId) {
        return ResponseEntity.ok(service.getApplication(applicationId));
    }

    @Override
    public ResponseEntity<Void> updateStatus(Long applicationId, String status) {
        service.updateStatus(applicationId, status);
        return ResponseEntity.ok().build();
    }
}
