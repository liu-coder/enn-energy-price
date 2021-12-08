package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/30 15:28
 * @description：快乐工作每一天
 */
public interface ExtElectricityPriceDictionaryMapper {
    List<ElectricityPriceDictionary> selectDictionaryByCondition(Map<String,Object> map);

    int updateSelectiveById(@Param("record") ElectricityPriceDictionary record);
}
