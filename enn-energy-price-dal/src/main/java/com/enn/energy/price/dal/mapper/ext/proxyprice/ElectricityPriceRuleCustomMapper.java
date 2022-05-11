package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 电价版本规则Mapper.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/29 18:21
 * @since : 1.0
 **/
@Mapper
public interface ElectricityPriceRuleCustomMapper {

    /**
     * 更具ruleId查询电价规则详情
     * @param ruleIdList
     * @return
     */
    List<ElectricityPriceRule> selectRulesByRuleIdList(List<String> ruleIdList);

    /**
     * 批量删除电价规则根据规则ids
     * @param map
     */
    void bacthDeletePriceRuleByRuleIds(Map<String,Object> map);

    /**
     * 根据体系id查找对应的规则列表
     * @param structureId
     * @return
     */
    List<ElectricityPriceRule> queryRuleListByStructureId(@Param( "structureId" ) String structureId);

    /**
     * 更新规则根据条件
     * @param electricityPriceRule
     * @return
     */
    int updateRuleByConditions(ElectricityPriceRule electricityPriceRule);
}
