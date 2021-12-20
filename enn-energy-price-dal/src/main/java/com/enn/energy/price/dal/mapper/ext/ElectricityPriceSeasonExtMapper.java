package com.enn.energy.price.dal.mapper.ext;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceSeason;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 电价季节mapper.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/1 14:07
 * @since : 1.0
 **/
@Mapper
public interface ElectricityPriceSeasonExtMapper {

    void batchAddElectricityPriceSeason(List<ElectricityPriceSeason> electricityPriceSeasonList);

    List<ElectricityPriceSeason> selectSeasonByCondition(Map<String, Object> map);

    List<String> selectSeasonGroupByCondition(Map<String,Object> map);

    int updateSeasonStateByVersionId(String versionId);
}
