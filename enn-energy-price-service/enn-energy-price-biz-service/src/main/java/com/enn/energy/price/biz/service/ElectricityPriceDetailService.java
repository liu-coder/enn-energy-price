package com.enn.energy.price.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enn.energy.price.biz.service.bo.ElectricityPriceDetailBO;
import com.enn.energy.price.biz.service.bo.ElectricityPriceRuleBO;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.dal.mapper.ext.ElectricityPriceDetailExtMapper;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceDetail;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/30 13:57
 * @description：快乐工作每一天
 */
@Service
public class ElectricityPriceDetailService {
    ThreadLocal<SimpleDateFormat> sf_mm_dd = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };

    @Autowired
    private ElectricityPriceDetailExtMapper electricityPriceDetailExtMapper;

    public List<ElectricityPriceDetailBO> selectDetailByCondition(String versionId, String ruleId, String seasonId) {
        Map<String,Object> map = new HashMap<>();
        map.put("ruleId",ruleId);
        map.put("seasonId",seasonId);
        map.put("versionId",versionId);
        map.put("state",0);
        List<ElectricityPriceDetail> priceDetails = electricityPriceDetailExtMapper.selectDetailByCondition(map);
        List<ElectricityPriceDetailBO> electricityPriceDetailBos = BeanUtil.mapList(priceDetails, ElectricityPriceDetailBO.class);
        removeThreadLocal();//移除threadLocal，预防内存溢出
        return electricityPriceDetailBos;
    }

    public List<ElectricityPriceDetailBO> selectDetailByCondition(String ruleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("ruleId",ruleId);
        map.put("state",0);
        List<ElectricityPriceDetail> priceDetails = electricityPriceDetailExtMapper.selectDetailByCondition(map);
        List<ElectricityPriceDetailBO> detailBos = BeanUtil.mapList(priceDetails, ElectricityPriceDetailBO.class);
        return detailBos;
    }
    private void removeThreadLocal(){
        sf_mm_dd.remove();
    }

    /**
     * 批量添加价格明细
     * @param electricityPriceDetailList
     */
    public void batchAddElectricityPriceDetail(List<ElectricityPriceDetail> electricityPriceDetailList) {
        if (CollectionUtil.isEmpty(electricityPriceDetailList)) {
            return;
        }
        electricityPriceDetailExtMapper.batchAddElectricityPriceDetail(electricityPriceDetailList);
    }

    /**
     * 根据版本id查询价格明细
     * @param versionId
     * @return
     */
    public List<ElectricityPriceDetail> selectPriceDetailsByVersionId(String versionId){
        return electricityPriceDetailExtMapper.selectPriceDetailsByVersionId(versionId);
    }


    public void batchUpdateByRuleIds(List<ElectricityPriceRuleBO> ruleBos) {
        if (CollectionUtil.isEmpty(ruleBos)) {
            return;
        }
        List<ElectricityPriceRule> rules = BeanUtil.mapList(ruleBos, ElectricityPriceRule.class);
        electricityPriceDetailExtMapper.batchUpdateByRuleIds(rules);
    }

    public int deleteDetailsByVersionId(String versionId) {
        return electricityPriceDetailExtMapper.deleteDetailsByVersionId(versionId);
    }
}
