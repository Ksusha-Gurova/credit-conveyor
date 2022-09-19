package ru.neoflex.gateway.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ApplicationApi;
import org.openapitools.model.FinishRegistrationRequestDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.service.GatewayApplicationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GatewayApplicationController implements ApplicationApi{

    private final GatewayApplicationService service;

    @Override
    public ResponseEntity<Void> applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("applyOffer(), loanOfferDTO = {}", loanOfferDTO);
        service.applyOffer(loanOfferDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<LoanOfferDTO>> createLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("createLoanApplication(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        return ResponseEntity.ok(service.createLoanApplication(loanApplicationRequestDTO));
    }

    @Override
    public ResponseEntity<Void> finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("finishRegistration(), applicationId = {}, finishRegistrationRequestDTO = {}", applicationId, finishRegistrationRequestDTO);
        service.finishRegistration(applicationId, finishRegistrationRequestDTO);
        return ResponseEntity.ok().build();
    }
}
