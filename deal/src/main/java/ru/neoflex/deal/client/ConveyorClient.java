package ru.neoflex.deal.client;

import org.openapitools.model.CreditDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.openapitools.model.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "conveyor", url = "${application.conveyorHost}")
public interface ConveyorClient {
//    @RequestMapping(method = RequestMethod.POST, value = "/conveyor/offers")
    @PostMapping(value = "/conveyor/offers")
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @RequestMapping(method = RequestMethod.POST, value = "/conveyor/calculation")
    CreditDTO getCreditDTO(ScoringDataDTO scoringDataDTO);
}
