package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import org.apache.ibatis.annotations.Mapper;

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
public interface ElectricityPriceRuleExtMapper {

    void updatePriceRuleState(String versionId);

    void batchAddElectricityPriceRule(List<ElectricityPriceRule> electricityPriceRuleList);

    List<ElectricityPriceRule> selectPriceRulesByVersionId(String versionId);

    List<ElectricityPriceRule> selectRuleByCondition(Map<String,Object> map);
}
