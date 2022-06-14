package ru.neoflex.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ApplicationApi;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.application.service.ApplicationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApplicationController implements ApplicationApi {
    private final ApplicationService applicationService;

    @Override
    public ResponseEntity<Void> applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("applyOffer(), loanOfferDTO = {}", loanOfferDTO);
        applicationService.applyOffer(loanOfferDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<LoanOfferDTO>> createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("createApplication(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        return ResponseEntity.ok(applicationService.createApplication(loanApplicationRequestDTO));
    }
}
