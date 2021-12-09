package com.enn.energy.price.dal.mapper.mbg;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceVersion;

public interface ElectricityPriceVersionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_version
     *
     * @mbg.generated Tue Dec 07 11:15:12 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_version
     *
     * @mbg.generated Tue Dec 07 11:15:12 CST 2021
     */
    int insert(ElectricityPriceVersion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_version
     *
     * @mbg.generated Tue Dec 07 11:15:12 CST 2021
     */
    int insertSelective(ElectricityPriceVersion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_version
     *
     * @mbg.generated Tue Dec 07 11:15:12 CST 2021
     */
    ElectricityPriceVersion selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_version
     *
     * @mbg.generated Tue Dec 07 11:15:12 CST 2021
     */
    int updateByPrimaryKeySelective(ElectricityPriceVersion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_version
     *
     * @mbg.generated Tue Dec 07 11:15:12 CST 2021
     */
    int updateByPrimaryKey(ElectricityPriceVersion record);
}