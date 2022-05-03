package com.enn.energy.price.dal.mapper.mbg;

import com.enn.energy.price.dal.po.mbg.ElectricityPrice;

public interface ElectricityPriceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    int insert(ElectricityPrice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    int insertSelective(ElectricityPrice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    ElectricityPrice selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    int updateByPrimaryKeySelective(ElectricityPrice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    int updateByPrimaryKey(ElectricityPrice record);
}