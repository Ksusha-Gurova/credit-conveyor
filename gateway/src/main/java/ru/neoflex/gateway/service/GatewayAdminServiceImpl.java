package ru.neoflex.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ApplicationDTO;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.client.DealClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAdminServiceImpl implements GatewayAdminService {

    private final DealClient client;

    @Override
    public List<ApplicationDTO> getAllApplication() {
        log.info("getAllApplication(), отправляем запрос в МС-deal на запрос всех кредитных заявок");
        return client.getAllApplication();
    }

    @Override
    public ApplicationDTO getApplication(Long applicationId) {
        log.info("getApplication(), отправляем запрос в МС-deal на запрос заявки с id = {}", applicationId);
        return client.getApplication(applicationId);
    }

    @Override
    public void updateStatus(Long applicationId, String status) {
        log.info("updateStatus(), отправляем запрос в МС-deal на изменение статуса заявки с id = {}, status = {}", applicationId, status);
        client.updateStatus(applicationId, status);
    }
}
