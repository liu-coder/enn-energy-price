package com.enn.energy.price.integration.cimzuul.client;


import com.enn.energy.price.integration.cimzuul.dto.CimResponse;
import com.enn.energy.price.integration.cimzuul.dto.EntDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Zhiqiang Li
 * @date 2021/12/1
 * @description:调用cim服务
 **/
@FeignClient(value = "BASE-CIM-ZUUL", url = "${cim.base.zuul.url}")
public interface CimBaseService {

    /**
     * 通过企业id获取cim系统的code接口
     * @param ownerEntId
     * @return
     */
    @GetMapping("/cim/api-service/system/sys/ownerEntId/{ownerEntId}")
    CimResponse<List<EntDTO>> querySystemInfoListByEntId(@PathVariable("ownerEntId") String ownerEntId);

}
