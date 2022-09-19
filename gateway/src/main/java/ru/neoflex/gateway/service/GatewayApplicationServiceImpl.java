package ru.neoflex.gateway.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.FinishRegistrationRequestDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.client.ApplicationClient;
import ru.neoflex.gateway.client.DealClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayApplicationServiceImpl implements GatewayApplicationService{

    private final ApplicationClient applicationClient;
    private final DealClient dealClient;

    @Override
    public List<LoanOfferDTO> createLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("createLoanApplication(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);

        try {
            List<LoanOfferDTO> applicationRequest = applicationClient.createApplicationRequest(loanApplicationRequestDTO);
            log.info("createLoanApplication(), отправляем запрос в MC-application на создание новой заявки");

           return applicationRequest;
        } catch (FeignException e) {
            log.error("createLoanApplication(), ошибка при обработке запроса на создание новой заявки: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.debug("applyOffer(), loanOfferDTO = {}", loanOfferDTO);

        try {
            applicationClient.applyOffer(loanOfferDTO);
            log.info("applyOffer(), отправляем запрос в MC-application с выбором одного из предложений");
        } catch (FeignException e) {
            log.error("applyOffer(), ошибка при обработке запроса с выбором одного из предложений: {}", e.getMessage());
        }
    }

    @Override
    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.debug("finishRegistration(), applicationId = {}, finishRegistrationRequestDTO = {}", applicationId, finishRegistrationRequestDTO);

        try {
            dealClient.calculateCreditRequest(applicationId, finishRegistrationRequestDTO);
            log.info("finishRegistration(), отправляем запрос в MC-deal ра расчет полных условий кредита");
        } catch (FeignException e) {
            log.error("finishRegistration(), ошибка при обработке запроса на расчет кредитных условий: {}", e.getMessage());
        }
    }
}
