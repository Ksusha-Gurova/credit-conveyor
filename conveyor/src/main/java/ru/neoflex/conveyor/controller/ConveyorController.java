package ru.neoflex.conveyor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ConveyorApi;
import org.openapitools.model.CreditDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.openapitools.model.ScoringDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.conveyor.service.ConveyorService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConveyorController implements ConveyorApi {
    private final ConveyorService conveyorService;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("calculateCreditOffers(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        return ResponseEntity.ok(conveyorService.calculateCreditOffers(loanApplicationRequestDTO));
    }

    @Override
    public ResponseEntity<CreditDTO> calculateCreditParameters(ScoringDataDTO scoringDataDTO) {
        log.info("calculateCreditParameters(), scoringDataDTO = {}", scoringDataDTO);
        return ResponseEntity.ok(conveyorService.calculateCreditParameters(scoringDataDTO));
    }
}
