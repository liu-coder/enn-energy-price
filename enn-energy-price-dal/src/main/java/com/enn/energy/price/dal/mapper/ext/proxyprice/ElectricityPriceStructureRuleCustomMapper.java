package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructureRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityPriceStructureRuleCustomMapper {
    /**
     * 增加体系规则
     * @param ectricityPriceStructureRule
     */
    void insertElectricityPriceStructureRule(ElectricityPriceStructureRule ectricityPriceStructureRule);

    /**
     * 根据体系规则ids批量删除体系规则
     * @param map
     */
    void batchDeleteStructureRuleByIds(Map<String,Object> map);


    /**
     * 根据体系id查找体系下面对应的体系规则id
     * @param map
     * @return
     */
    List<ElectricityPriceStructureRule> queryElectricityPriceRulesByCondition(Map<String,Object> map);


    /**
     * 根据价格规则id查询规则
     * @param structureRuleIds
     * @return
     */
    List<ElectricityPriceStructureRule> queryElectricityPriceRulesByStructureRule(@Param( "structureRuleIds" ) String structureRuleIds);
}
