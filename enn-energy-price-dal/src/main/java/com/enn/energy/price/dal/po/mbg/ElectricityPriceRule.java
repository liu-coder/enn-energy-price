package com.enn.energy.price.dal.po.mbg;

import java.util.Date;

public class ElectricityPriceRule {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.rule_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String ruleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.version_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String versionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.industry
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String industry;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.strategy
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String strategy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.voltage_level
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String voltageLevel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.transformer_capacity_price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String transformerCapacityPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.max_capacity_price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String maxCapacityPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.state
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.create_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_rule.update_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.id
     *
     * @return the value of electricity_price_rule.id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.id
     *
     * @param id the value for electricity_price_rule.id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.rule_id
     *
     * @return the value of electricity_price_rule.rule_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.rule_id
     *
     * @param ruleId the value for electricity_price_rule.rule_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.version_id
     *
     * @return the value of electricity_price_rule.version_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.version_id
     *
     * @param versionId the value for electricity_price_rule.version_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.industry
     *
     * @return the value of electricity_price_rule.industry
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.industry
     *
     * @param industry the value for electricity_price_rule.industry
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.strategy
     *
     * @return the value of electricity_price_rule.strategy
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.strategy
     *
     * @param strategy the value for electricity_price_rule.strategy
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy == null ? null : strategy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.voltage_level
     *
     * @return the value of electricity_price_rule.voltage_level
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getVoltageLevel() {
        return voltageLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.voltage_level
     *
     * @param voltageLevel the value for electricity_price_rule.voltage_level
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel == null ? null : voltageLevel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.transformer_capacity_price
     *
     * @return the value of electricity_price_rule.transformer_capacity_price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getTransformerCapacityPrice() {
        return transformerCapacityPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.transformer_capacity_price
     *
     * @param transformerCapacityPrice the value for electricity_price_rule.transformer_capacity_price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setTransformerCapacityPrice(String transformerCapacityPrice) {
        this.transformerCapacityPrice = transformerCapacityPrice == null ? null : transformerCapacityPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.max_capacity_price
     *
     * @return the value of electricity_price_rule.max_capacity_price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getMaxCapacityPrice() {
        return maxCapacityPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.max_capacity_price
     *
     * @param maxCapacityPrice the value for electricity_price_rule.max_capacity_price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setMaxCapacityPrice(String maxCapacityPrice) {
        this.maxCapacityPrice = maxCapacityPrice == null ? null : maxCapacityPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.state
     *
     * @return the value of electricity_price_rule.state
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.state
     *
     * @param state the value for electricity_price_rule.state
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.create_time
     *
     * @return the value of electricity_price_rule.create_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.create_time
     *
     * @param createTime the value for electricity_price_rule.create_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_rule.update_time
     *
     * @return the value of electricity_price_rule.update_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_rule.update_time
     *
     * @param updateTime the value for electricity_price_rule.update_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}