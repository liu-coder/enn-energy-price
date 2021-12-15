package com.enn.energy.price.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDetailCache;
import com.enn.energy.price.core.service.impl.PriceCacheClientImpl;
import com.enn.energy.price.biz.service.ElectricityPriceRuleService;
import com.enn.energy.price.common.constants.CommonConstant;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 测试.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/3 16:28
 * @since : 1.0
 **/
@RestController
@Slf4j
public class PriceControllerTest {

    @Autowired
    private PriceCacheClientImpl priceCacheClientImpl;

    @Autowired
    private ElectricityPriceRuleService electricityPriceRuleService;

    @RequestMapping("/test")
    public String getDataFromCache(@RequestBody @NotNull String key) {


//        List<ElectricityPriceRule> list = electricityPriceRuleService.selectPriceRulesByVersionId("www2www");
//        System.out.println(list.size());
//        key = "cimcode_shebeiid";
//        Set<Object> fields = priceCacheClientImpl.hashKeys(key, CommonConstant.ELECTRICITY_PRICE);
//
//        if (CollectionUtil.isNotEmpty(fields)) {
//            for (Object hKey : fields) {
//               // priceCacheClientImpl.hDelete(key, CommonConstant.ELECTRICITY_PRICE, (String) hKey);
//             String value =  priceCacheClientImpl.hGet(key,CommonConstant.ELECTRICITY_PRICE,(String) hKey);
//                List<ElectricityPriceDetailCache> cacheList =  JSON.parseArray(value,ElectricityPriceDetailCache.class);
//             // List<ElectricityPriceDetailCache> list = JSON.parseArray(value,ElectricityPriceDetailCache.class);
//            }
//        }

        priceCacheClientImpl.vSet("test","ep","hhhhhhh");
        log.error("=========,{}", (String) priceCacheClientImpl.vGet("test","ep"));

        return null;
    }


}
