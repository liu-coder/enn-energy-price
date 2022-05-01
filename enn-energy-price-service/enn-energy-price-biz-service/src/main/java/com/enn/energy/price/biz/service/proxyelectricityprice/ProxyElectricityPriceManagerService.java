package com.enn.energy.price.biz.service.proxyelectricityprice;

import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * 代购电价service
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public interface ProxyElectricityPriceManagerService {

    RdfaResult<Boolean> createPriceVersionStructures();

}
