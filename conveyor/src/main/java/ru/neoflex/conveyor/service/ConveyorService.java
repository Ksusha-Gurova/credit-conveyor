package ru.neoflex.conveyor.service;

import org.openapitools.model.CreditDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.openapitools.model.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    CreditDTO calculateCreditParameters(ScoringDataDTO scoringDataDTO);
}
