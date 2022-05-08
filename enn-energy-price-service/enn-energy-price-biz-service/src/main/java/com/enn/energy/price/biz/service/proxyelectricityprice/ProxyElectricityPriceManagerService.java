package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.proxyprice.*;
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


    /**
     * 删除电价版本
     * @param electricityPriceVersionDeleteBO
     * @return
     */
    RdfaResult<Boolean> deletePriceVersion(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO);


    /**
     * 根据省编码查询价格版本列表
     * @param provinceCode
     * @return
     */
    RdfaResult<ElectricityPriceVersionListBO> queryPriceVersionList(String provinceCode);


    /**
     * 根据版本id查询体系列表
     * @param versionId
     * @return
     */
    RdfaResult<ElectricityPriceStructureListBO> queryPriceVersionStructureList(String versionId);


    /**
     * 根据体系id查询体系详情
     * @param structuredId
     */
    RdfaResult<ElectricityPriceStructureDetailBO> getStructureDetail(String structuredId);
}
