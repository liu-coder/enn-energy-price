package com.enn.energy.price.biz.service.proxyelectricityprice;

import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.biz.service.bo.proxyprice.*;
import top.rdfa.framework.biz.ro.RdfaResult;

import java.util.List;
import java.util.Map;

/**
 * 代购电价service
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public interface ProxyElectricityPriceManagerService {


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
     * 电价版本删除校验
     * @param electricityPriceVersionDeleteBO
     * @return
     */
    RdfaResult<Boolean> versionDeleteValidate(ElectricityPriceVersionDeleteBO electricityPriceVersionDeleteBO);

    /**
     * 体系删除校验
     * @param structureDeleteValidateBO
     * @return
     */
    RdfaResult<Boolean> structureDeleteValidate(ElectricityPriceStructureDeleteValidateBO structureDeleteValidateBO);


    Map<Integer, List<ElectricityPriceDictionaryBO>> getPriceElectricityDictionaries(String type, String province);
}
