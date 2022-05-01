package com.enn.energy.price.biz.service.proxyelectricityprice.impl;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionStructuresCreateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionUpdateBO;
import com.enn.energy.price.biz.service.proxyelectricityprice.ProxyElectricityPriceManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.annotation.Resource;

/**
 * 代购电价service实现
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
@Service
@Slf4j
public class ProxyElectricityPriceManagerServiceImpl implements ProxyElectricityPriceManagerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPriceVersionStructures(ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateBO) {

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RdfaResult updatePriceVersion(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO) {



        return null;
    }
}