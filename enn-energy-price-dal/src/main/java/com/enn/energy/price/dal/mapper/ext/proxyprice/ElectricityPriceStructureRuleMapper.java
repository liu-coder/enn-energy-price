package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructureRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ElectricityPriceStructureRuleMapper {
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


    /**
     * 根据体系id查找体系下面对应的体系规则id
     * @param structureId
     * @return
     */
    List<ElectricityPriceStructureRule> queryElectricityPriceRulesByStructureId(@Param( "structureId" ) String structureId);


    /**
     * 根据价格规则id查询规则
     * @param structureRuleIds
     * @return
     */
    List<ElectricityPriceStructureRule> queryElectricityPriceRulesByStructureRule(@Param( "structureRuleIds" ) String structureRuleIds);
}
