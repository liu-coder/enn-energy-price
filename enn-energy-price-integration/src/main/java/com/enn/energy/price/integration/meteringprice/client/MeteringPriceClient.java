package com.enn.energy.price.integration.meteringprice.client;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import com.enn.energy.price.integration.meteringprice.dto.MeteringPriceReqDto;
import com.enn.energy.price.integration.meteringprice.dto.MeteringPriceRespDto;

/**
 * @author 
 * @date 
 * @description:metering-prepaid API 单一用能
 * http://metering-prepaid.test.fnwintranet.com/doc.html  /prepaid/price/queryCustomElectricPrice
 * 需要去uac拿ticket鉴权，或者固定的123456
 **/
//@FeignClient(name="MeteringPriceClient",contextId = "MeteringPriceClient",path="/prepaid", url = "${metering.api.server.url}")

@FeignClient(name="MeteringPriceClient",contextId = "MeteringPriceClient",path="/prepaid", url = "http://metering-prepaid.test.fnwintranet.com")
@RequestMapping(headers = { "ticket=123456", "Content-Type=application/json" })
public interface MeteringPriceClient {

    /**
     * 通过企业id获取cim系统的code接口
     * @param ownerEntId
     * @return
     */
    @GetMapping("/price/queryCustomElectricPrice")
	CimResponse<List<MeteringPriceRespDto>> queryMeteringPrice(MeteringPriceReqDto meteringPriceReqDto);

}
