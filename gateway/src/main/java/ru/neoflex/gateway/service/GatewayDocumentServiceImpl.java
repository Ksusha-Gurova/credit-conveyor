package ru.neoflex.gateway.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.client.DealClient;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayDocumentServiceImpl implements GatewayDocumentService{

    private final DealClient dealClient;
    private final GatewayAdminService adminService;

    @Override
    public void createDocumentRequest(Long applicationId) {
        log.debug("createDocumentRequest(), applicationId = {}", applicationId);

        dealClient.createDocumentRequest(applicationId);
        log.info("createDocumentRequest(), отправляем запрос в MC-deal на создание документов");
    }

    @Override
    public void signDocumentRequest(Long applicationId) {
        log.debug("signDocumentRequest(), applicationId = {}", applicationId);

            dealClient.signDocumentRequest(applicationId);
            log.info("signDocumentRequest(), отправляем запрос в MC-deal на подписание документов");
    }

    @Override
    public void verifySesCodeRequest(Long applicationId, String ses) {
        log.debug("verifySesCodeRequest(), applicationId = {}, ses code = {}", applicationId, ses);

        try {
            dealClient.verifySesCodeRequest(applicationId, ses);
            log.info("verifySesCodeRequest(), отправляем запрос в MC-deal на проверку электронной подписи");

        } catch (FeignException e){
            log.error("verifySesCodeRequest(), ошибка при обработке запроса на проверку ses-кода");
        }
    }

    @Override
    public void denyOnApplication(Long applicationId) {
        log.debug("denyOnApplication(), applicationId = {}", applicationId);

        adminService.updateStatus(applicationId, "CLIENT_DENIED");
        log.info("denyOnApplication(), отправляем запрос на отказ клиентом от заявки {}", applicationId);
    }
}
