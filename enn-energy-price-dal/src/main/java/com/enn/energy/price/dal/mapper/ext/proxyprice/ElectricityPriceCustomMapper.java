package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.ext.ElectricityPriceDetailPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ElectricityPriceCustomMapper {

    /**
     * 根据价格ids删除价格
     * @param priceIds  价格ids
     */
    void batchDeletePriceByPriceIds(@Param( "priceIds" ) String priceIds);

    /**
     * 根据规则ids批量删除价格
     * @param ruleIds
     */
    void batchDeletePriceByRuleIds(@Param( "ruleIds" ) String ruleIds);


    /**
     * 获取电价明细列表
     * @param structureId
     * @return
     */
    List<ElectricityPriceDetailPO> getPriceDetailListByStructureId(@Param( "structureId" ) String structureId);
}
