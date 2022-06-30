package ru.neoflex.gateway.service;

import org.openapitools.model.FinishRegistrationRequestDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;

import java.util.List;

public interface GatewayApplicationService {
    List<LoanOfferDTO> createLoanApplication(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void applyOffer(LoanOfferDTO loanOfferDTO);

    void finishRegistration(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO);

}
