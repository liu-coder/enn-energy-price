package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionStructuresCreateBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceVersionUpdateBO;
import top.rdfa.framework.biz.ro.RdfaResult;

/**
 * 代购电价service
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public interface ProxyElectricityPriceManagerService {

    /**
     * @describtion  新建版本以及版本下的所有体系
     * @author sunjidong
     * @date 2022/5/1 9:57
     * @param priceVersionStructuresCreateBO
     * @return Boolean
     */
    Boolean createPriceVersionStructures(ElectricityPriceVersionStructuresCreateBO priceVersionStructuresCreateBO);

    /**
     * params  体系对象
     * @return boolean
     */
    RdfaResult<Boolean> updatePriceVersion(ElectricityPriceVersionUpdateBO electricityPriceVersionUpdateBO);

}
