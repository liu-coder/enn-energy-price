package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.ext.CityCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityCustomMapper {
    /**
     * 查询城市列表
     * @return 城市编码
     */
    List<CityCode> queryCityList(CityCode cityCode);


}
