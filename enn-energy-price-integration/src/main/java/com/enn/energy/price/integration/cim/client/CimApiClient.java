package com.enn.energy.price.integration.cim.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enn.energy.price.integration.cim.dto.CimPriceReq;
import com.enn.energy.price.integration.participant.dto.ParticipantResponse;

//@FeignClient(name="CimApiClient",contextId = "CimApiClient",path="/etsp", url = "${cim.api.server.url}")
@RequestMapping(headers = "X-GW-AccessKey=${participant.gateway.accessKey}")
public interface CimApiClient {

	/**
     *通过租户拿到uac企业的id
     * @param tenantId
     * @param source
     * @return
     */
    @PostMapping("/upower/getEnergyPrice/day")
    ParticipantResponse<List<String>> getDayEnergyPrice(@RequestBody CimPriceReq cimPriceReq);
}
