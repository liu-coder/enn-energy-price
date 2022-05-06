package com.enn.energy.price.dal.mapper.ext.proxyprice;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ElectricityPriceExtMapper {

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
}
