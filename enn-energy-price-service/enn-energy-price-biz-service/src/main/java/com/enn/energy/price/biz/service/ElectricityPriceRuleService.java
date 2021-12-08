package com.enn.energy.price.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceRuleBO;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.dal.mapper.ext.ElectricityPriceRuleExtMapper;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电价版本规则service.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/29 18:32
 * @since : 1.0
 **/
@Service
public class ElectricityPriceRuleService {

    @Autowired
    private ElectricityPriceRuleExtMapper electricityPriceRuleExtMapper;

    /**
     * 批量添加版本规则
     * @param electricityPriceRuleList
     */
    public void batchAddElectricityPriceRule(List<ElectricityPriceRule> electricityPriceRuleList){
        if(CollectionUtil.isEmpty(electricityPriceRuleList)){
            return;
        }
        electricityPriceRuleExtMapper.batchAddElectricityPriceRule(electricityPriceRuleList);
    }

    /**
     * 根据价格版本id查询价格规则
     * @param versionId
     * @return
     */
    public List<ElectricityPriceRule> selectPriceRulesByVersionId(String versionId){

        return electricityPriceRuleExtMapper.selectPriceRulesByVersionId(versionId);

    }



    /**
     * 根据规则ID查询电价规则
     * @param ruleId
     * @return
     */
    public ElectricityPriceRuleBO selectRuleByRuleId(String ruleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("ruleId",ruleId);
        map.put("state",0);
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.selectRuleByCondition(map);
        if (electricityPriceRules.size() == 0){
            return null;
        }
        ElectricityPriceRuleBO ruleBo = BeanUtil.map(electricityPriceRules.get(0), ElectricityPriceRuleBO.class);
        return ruleBo;
    }

    /**
     * 根据规则ID、版本ID查询规则
     * @param ruleId
     * @param versionId
     * @return
     */
    public ElectricityPriceRuleBO selectRuleByCondition(String ruleId,String versionId) {
        Map<String,Object> map = new HashMap<>();
        map.put("ruleId",ruleId);
        map.put("versionId",versionId);
        map.put("state",0);
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.selectRuleByCondition(map);
        if (electricityPriceRules.size() == 0){
            return null;
        }
        ElectricityPriceRuleBO ruleBo = BeanUtil.map(electricityPriceRules.get(0), ElectricityPriceRuleBO.class);
        return ruleBo;
    }

    public List<ElectricityPriceRuleBO> selectRuleListByVersionId(String versionId) {
        Map<String,Object> map = new HashMap<>();
        map.put("versionId",versionId);
        map.put("state",0);
        List<ElectricityPriceRule> electricityPriceRules = electricityPriceRuleExtMapper.selectRuleByCondition(map);
        List<ElectricityPriceRuleBO> ruleBos = BeanUtil.mapList(electricityPriceRules, ElectricityPriceRuleBO.class);
        return ruleBos;
    }


    /**
     * 更新价格规则状态
     *
     * @param versionId
     */
    public void updatePriceRuleState(String versionId) {
        electricityPriceRuleExtMapper.updatePriceRuleState(versionId);
    }
}
