package com.enn.energy.price.dal.mapper.mbg;

import com.enn.energy.price.dal.po.mbg.CityCode;

public interface CityCodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_code
     *
     * @mbg.generated Fri Apr 29 17:23:04 CST 2022
     */
    int deleteByPrimaryKey(String pcode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_code
     *
     * @mbg.generated Fri Apr 29 17:23:04 CST 2022
     */
    int insert(CityCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_code
     *
     * @mbg.generated Fri Apr 29 17:23:04 CST 2022
     */
    int insertSelective(CityCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_code
     *
     * @mbg.generated Fri Apr 29 17:23:04 CST 2022
     */
    CityCode selectByPrimaryKey(String pcode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_code
     *
     * @mbg.generated Fri Apr 29 17:23:04 CST 2022
     */
    int updateByPrimaryKeySelective(CityCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table city_code
     *
     * @mbg.generated Fri Apr 29 17:23:04 CST 2022
     */
    int updateByPrimaryKey(CityCode record);
}