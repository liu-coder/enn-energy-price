package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.List;

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
    List<ElectricityPriceVersionBO> queryPriceVersionList(String provinceCode);


    /**
     * 根据版本id查询体系列表
     * @param versionId
     * @return
     */
    List<ElectricityPriceStructureBO> queryPriceVersionStructureList(String versionId);


    /**
     * 根据体系id查询体系详情
     * @param structuredId
     */
    ElectricityPriceStructureDetailBO getStructureDetail(String structuredId);


    /**
     * 根据类型获取对应的字典列表
     * @param type
     */
    List<ElectricityPriceDictionaryBO> getPriceElectricityDictionarys(String type);
}
