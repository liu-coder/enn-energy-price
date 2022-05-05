package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructureRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ElectricityPriceStructureRuleExtMapper {
    /**
     * 增加体系规则
     * @param ectricityPriceStructureRule
     */
    void insertElectricityPriceStructureRule(ElectricityPriceStructureRule ectricityPriceStructureRule);

    /**
     * 根据体系规则ids批量删除体系规则
     * @param ids
     */
    void batchDeleteStructureRuleByIds(@Param( "ids" ) String ids);
}
