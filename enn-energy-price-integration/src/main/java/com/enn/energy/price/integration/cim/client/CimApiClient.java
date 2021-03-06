package com.enn.energy.price.integration.cim.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enn.energy.price.integration.cim.dto.CimPriceReq;
import com.enn.energy.price.integration.cim.dto.CimPriceResp;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;

@FeignClient(name = "CimApiClient", contextId = "CimApiClient", path = "/etsp", url = "${cim.api.server.url}")
//@RequestMapping(headers="Content-Type=application/json")
public interface CimApiClient {

	/**
     *
     * @param cimPriceReq
     * @return
     */
    @PostMapping("/upower/getEnergyPrice/day")
    CimResponse<CimPriceResp> getDayEnergyPrice( CimPriceReq cimPriceReq);
}
