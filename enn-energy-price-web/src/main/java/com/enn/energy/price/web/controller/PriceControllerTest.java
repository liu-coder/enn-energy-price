package com.enn.energy.price.web.controller;

import com.enn.energy.price.core.service.impl.PriceCacheClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

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

    @RequestMapping("/deleteCache")
    public String deleteCache(@NotNull String key) {


//        Set<Object> fields = priceCacheClientImpl.hashKeys(key, CommonConstant.ELECTRICITY_PRICE);
//        if (CollectionUtil.isNotEmpty(fields)) {
//            for (Object hKey : fields) {
//                priceCacheClientImpl.hDelete(key, CommonConstant.ELECTRICITY_PRICE, (String) hKey);
//            }
//        }
        priceCacheClientImpl.flushDB();
        return "删除缓存成功";
    }

}
