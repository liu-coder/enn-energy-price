package com.enn.energy.price.dal.mapper.mbg;

import com.enn.energy.price.dal.po.mbg.CityWeather;

public interface CityWeatherMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_weather
     *
     * @mbg.generated Sat May 07 11:04:24 CST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_weather
     *
     * @mbg.generated Sat May 07 11:04:24 CST 2022
     */
    int insert(CityWeather record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_weather
     *
     * @mbg.generated Sat May 07 11:04:24 CST 2022
     */
    int insertSelective(CityWeather record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_weather
     *
     * @mbg.generated Sat May 07 11:04:24 CST 2022
     */
    CityWeather selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_weather
     *
     * @mbg.generated Sat May 07 11:04:24 CST 2022
     */
    int updateByPrimaryKeySelective(CityWeather record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_weather
     *
     * @mbg.generated Sat May 07 11:04:24 CST 2022
     */
    int updateByPrimaryKey(CityWeather record);
}