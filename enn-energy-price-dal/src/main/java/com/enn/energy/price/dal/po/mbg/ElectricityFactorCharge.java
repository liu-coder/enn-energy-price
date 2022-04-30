package com.enn.energy.price.dal.po.mbg;

import java.math.BigDecimal;

public class ElectricityFactorCharge {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_factor_charge.id
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_factor_charge.standard_factor
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    private BigDecimal standardFactor;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_factor_charge.actual_factor
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    private BigDecimal actualFactor;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_factor_charge.numerial
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    private BigDecimal numerial;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_factor_charge.id
     *
     * @return the value of electricity_factor_charge.id
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_factor_charge.id
     *
     * @param id the value for electricity_factor_charge.id
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_factor_charge.standard_factor
     *
     * @return the value of electricity_factor_charge.standard_factor
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public BigDecimal getStandardFactor() {
        return standardFactor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_factor_charge.standard_factor
     *
     * @param standardFactor the value for electricity_factor_charge.standard_factor
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public void setStandardFactor(BigDecimal standardFactor) {
        this.standardFactor = standardFactor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_factor_charge.actual_factor
     *
     * @return the value of electricity_factor_charge.actual_factor
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public BigDecimal getActualFactor() {
        return actualFactor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_factor_charge.actual_factor
     *
     * @param actualFactor the value for electricity_factor_charge.actual_factor
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public void setActualFactor(BigDecimal actualFactor) {
        this.actualFactor = actualFactor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_factor_charge.numerial
     *
     * @return the value of electricity_factor_charge.numerial
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public BigDecimal getNumerial() {
        return numerial;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_factor_charge.numerial
     *
     * @param numerial the value for electricity_factor_charge.numerial
     *
     * @mbg.generated Fri Apr 29 17:34:55 CST 2022
     */
    public void setNumerial(BigDecimal numerial) {
        this.numerial = numerial;
    }
}