package ru.neoflex.dossier.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.neoflex.dossier.client.dto.ApplicationDTO;

@FeignClient(value = "deal", url = "${application.dealHost}")
public interface DealClient {
    @RequestMapping(method = RequestMethod.GET, value = "/deal/admin/application/{applicationId}")
    ApplicationDTO getApplication(@PathVariable Long applicationId);

    @RequestMapping(method = RequestMethod.PUT, value = "/deal/admin/application/{applicationId}/status")
    void updateStatus(@PathVariable Long applicationId);
}
