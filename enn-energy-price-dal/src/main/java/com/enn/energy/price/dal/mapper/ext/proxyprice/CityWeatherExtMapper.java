package com.enn.energy.price.dal.mapper.ext.proxyprice;

import com.enn.energy.price.dal.po.mbg.CityWeather;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityWeatherExtMapper {

    int batchInsertOrUpdate(List<CityWeather> cityWeatherList);
}