package ru.neoflex.application.client;

import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "deal", url = "http://localhost:8081/")
public interface DealClient {
    @RequestMapping(method = RequestMethod.POST, value = "/deal/application")
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/deal/offer")
    void applyOffer(LoanOfferDTO loanOfferDTO);
}