package ru.neoflex.application.service;

import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    void applyOffer(LoanOfferDTO loanOfferDTO);

    List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
