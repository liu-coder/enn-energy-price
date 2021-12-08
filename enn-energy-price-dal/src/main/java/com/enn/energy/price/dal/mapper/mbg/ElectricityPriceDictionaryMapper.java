package com.enn.energy.price.dal.mapper.mbg;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceDictionary;

public interface ElectricityPriceDictionaryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_dictionary
     *
     * @mbg.generated Tue Nov 30 14:11:02 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_dictionary
     *
     * @mbg.generated Tue Nov 30 14:11:02 CST 2021
     */
    int insert(ElectricityPriceDictionary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_dictionary
     *
     * @mbg.generated Tue Nov 30 14:11:02 CST 2021
     */
    int insertSelective(ElectricityPriceDictionary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_dictionary
     *
     * @mbg.generated Tue Nov 30 14:11:02 CST 2021
     */
    ElectricityPriceDictionary selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_dictionary
     *
     * @mbg.generated Tue Nov 30 14:11:02 CST 2021
     */
    int updateByPrimaryKeySelective(ElectricityPriceDictionary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table electricity_price_dictionary
     *
     * @mbg.generated Tue Nov 30 14:11:02 CST 2021
     */
    int updateByPrimaryKey(ElectricityPriceDictionary record);
}