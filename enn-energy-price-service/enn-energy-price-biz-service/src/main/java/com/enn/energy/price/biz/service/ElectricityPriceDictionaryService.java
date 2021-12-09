package com.enn.energy.price.biz.service;


import com.enn.energy.price.biz.service.bo.ElectricityPriceDictionaryBO;
import com.enn.energy.price.common.utils.BeanUtil;
import com.enn.energy.price.dal.mapper.ext.ExtElectricityPriceDictionaryMapper;
import com.enn.energy.price.dal.mapper.mbg.ElectricityPriceDictionaryMapper;
import com.enn.energy.price.dal.po.mbg.ElectricityPriceDictionary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/22 16:51
 * @description：快乐工作每一天
 */
@Service
@Slf4j
public class ElectricityPriceDictionaryService{

    @Autowired
    private ExtElectricityPriceDictionaryMapper extElectricityPriceDictionaryMapper;
    @Autowired
    private ElectricityPriceDictionaryMapper electricityPriceDictionaryMapper;

    public int insertDictionary(ElectricityPriceDictionaryBO priceDictionaryBo) {
        //创建之前先查询 根据code和type唯一性
        ElectricityPriceDictionary electricityPriceDictionary = selectByCodeAndType(priceDictionaryBo.getCode(), priceDictionaryBo.getType());
        ElectricityPriceDictionary selectByNameAndType = selectByNameAndType(priceDictionaryBo.getName(), priceDictionaryBo.getType());
        if (electricityPriceDictionary != null || selectByNameAndType != null){
            return -99;
        }
        priceDictionaryBo.setState(0);
        priceDictionaryBo.setCreateTime(new Date());
        priceDictionaryBo.setUpdateTime(new Date());
        ElectricityPriceDictionary dictionary = BeanUtil.map(priceDictionaryBo, ElectricityPriceDictionary.class);
        return electricityPriceDictionaryMapper.insert(dictionary);
    }

    public ElectricityPriceDictionary selectByCodeAndType(String code, Integer type) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",code);
        map.put("type",type);
        map.put("state",0);
        List<ElectricityPriceDictionary> dictionaries = extElectricityPriceDictionaryMapper.selectDictionaryByCondition(map);
        return dictionaries.size() == 0 ? null : dictionaries.get(0);
    }

    public ElectricityPriceDictionary selectByNameAndType(String name, Integer type) {
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("type",type);
        map.put("state",0);
        List<ElectricityPriceDictionary> dictionaries = extElectricityPriceDictionaryMapper.selectDictionaryByCondition(map);
        return dictionaries.size() == 0 ? null : dictionaries.get(0);
    }
    public List<ElectricityPriceDictionaryBO> selectDictionary(ElectricityPriceDictionaryBO bo) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",bo.getId());
        map.put("code",bo.getCode());
        map.put("type",bo.getType());
        map.put("name",bo.getName());
        map.put("state",bo.getState());
        map.put("typeDesc",bo.getTypeDesc());
        List<ElectricityPriceDictionary> electricityPriceDictionaries = extElectricityPriceDictionaryMapper.selectDictionaryByCondition(map);
        List<ElectricityPriceDictionaryBO> dictionaryBOList = BeanUtil.mapList(electricityPriceDictionaries, ElectricityPriceDictionaryBO.class);
        return dictionaryBOList;
    }

    public int editDictionary(ElectricityPriceDictionaryBO bo) {
        ElectricityPriceDictionary record = new ElectricityPriceDictionary();
        record.setCode(bo.getCode());
        record.setName(bo.getName());
        record.setType(bo.getType());
        record.setState(bo.getState());
        record.setId(bo.getId());
        record.setUpdateTime(new Date());
        int result = extElectricityPriceDictionaryMapper.updateSelectiveById(record);
        return result;
    }
}
