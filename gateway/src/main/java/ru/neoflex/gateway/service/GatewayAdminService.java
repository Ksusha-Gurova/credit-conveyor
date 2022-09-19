package ru.neoflex.gateway.service;

import org.openapitools.model.ApplicationDTO;

import java.util.List;

public interface GatewayAdminService {
    List<ApplicationDTO> getAllApplication();

    ApplicationDTO getApplication(Long applicationId);

    void updateStatus(Long applicationId, String status);
}
