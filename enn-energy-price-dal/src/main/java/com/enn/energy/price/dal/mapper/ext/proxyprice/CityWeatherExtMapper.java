package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.CityWeather;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityWeatherExtMapper {

    int insertOrUpdate(CityWeather record);


}