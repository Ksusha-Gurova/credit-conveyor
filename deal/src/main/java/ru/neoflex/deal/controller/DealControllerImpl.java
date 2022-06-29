package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.DealApi;
import org.openapitools.model.ApplicationDTO;
import org.openapitools.model.FinishRegistrationRequestDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.service.DealService;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealApi {
    private final DealService dealService;

    @Override
    public ResponseEntity<Void> applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("applyOffer(), loanOfferDTO = {}", loanOfferDTO);
        dealService.applyOffer(loanOfferDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> calculateCredit(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("calculateCredit(), applicationId = {}, finishRegistrationRequestDTO = {}", applicationId, finishRegistrationRequestDTO);
        dealService.calculateCredit(applicationId, finishRegistrationRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<LoanOfferDTO>> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("calculateCreditOffers(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        return ResponseEntity.ok(dealService.calculateCreditOffers(loanApplicationRequestDTO));
    }

    @Override
    public ResponseEntity<Void> sendDocuments(Long applicationId) {
        log.info("sendDocuments(), applicationId = {}", applicationId);
        dealService.sendDocuments(applicationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signDocuments(Long applicationId) {
        log.info("signDocuments(), applicationId = {}", applicationId);
        dealService.signDocuments(applicationId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signDocumentsSesCode(Long applicationId, String sesCode) {
        log.info("signDocumentsSesCode(), applicationId = {}, sesCode = {}", applicationId, sesCode);
        dealService.signDocumentsSesCode(applicationId, sesCode);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApplicationDTO> getApplication(Long applicationId) {
        log.info("getApplication(), applicationId = {}", applicationId);
        return ResponseEntity.ok(dealService.getApplication(applicationId));
    }

    @Override
    public ResponseEntity<Void> updateStatus(Long applicationId, String status) {
        log.info("updateStatus(), applicationId = {}, status = {}", applicationId, status);
        dealService.updateStatus(applicationId, status);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ApplicationDTO>> getAllApplication() {
        log.info("getAllApplication()");
        return ResponseEntity.ok(dealService.getAllApplication());
    }
}
