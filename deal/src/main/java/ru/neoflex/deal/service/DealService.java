package ru.neoflex.deal.service;

import org.openapitools.model.FinishRegistrationRequestDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import java.util.List;

public interface DealService {
    void applyOffer(LoanOfferDTO loanOfferDTO);

    void calculateCredit(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO);

    List<LoanOfferDTO> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void sendDocuments(Long applicationId);

    void signDocuments(Long applicationId);

    void signDocumentsSesCode(Long applicationId);
}
