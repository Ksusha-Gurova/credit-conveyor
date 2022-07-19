package ru.neoflex.gateway.client;

import org.openapitools.model.FinishRegistrationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "deal", url = "${application.dealHost}")
public interface DealClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/deal/calculate/{applicationId}")
    void calculateCreditRequest(@PathVariable Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO);

    @RequestMapping(method = RequestMethod.POST, value = "/deal/document/{applicationId}/send")
    void createDocumentRequest(@PathVariable Long applicationId);

    @RequestMapping(method = RequestMethod.POST, value = "/deal/document/{applicationId}/sign")
    void signDocumentRequest(@PathVariable Long applicationId);

    @RequestMapping(method = RequestMethod.POST, value = "/deal/document/{applicationId}/code")
    void verifySesCodeRequest(@PathVariable Long applicationId, String ses);

    @RequestMapping(method = RequestMethod.PUT, value = "/deal/admin/application/{applicationId}/status")
    void applicationDenialRequest(@PathVariable Long applicationId, String statusClientDenied);
}
