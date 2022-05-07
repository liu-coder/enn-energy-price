package com.enn.energy.price.integration.cim.client;

import com.enn.energy.price.integration.cim.dto.CimDayPointPriceResp;
import com.enn.energy.price.integration.cim.dto.CimPointPriceReq;
import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(name = "cimPriceClient", url = "${cim.api.server.url}")
public interface CimPriceClient {
    /**
     * 根据测点查询某天电价
     * @param cimPriceReq
     * @return
     */
    @PostMapping("/etsp/upower/pointRecord/energyPrice/day")
    CimResponse<CimDayPointPriceResp> getDayPointRecord(CimPointPriceReq cimPriceReq);
}
