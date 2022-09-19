package ru.neoflex.gateway.client;

import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "application", url = "${application.applicationHost}")
public interface ApplicationClient {
    @RequestMapping(method = RequestMethod.POST, value = "/application")
    List<LoanOfferDTO> createApplicationRequest(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/application/offer")
    void applyOffer(LoanOfferDTO loanOfferDTO);
}
