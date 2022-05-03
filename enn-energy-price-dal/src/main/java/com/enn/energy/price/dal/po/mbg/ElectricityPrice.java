package com.enn.energy.price.dal.po.mbg;

import java.util.Date;

public class ElectricityPrice {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.detail_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String detailId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.season_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String seasonId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.rule_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String ruleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.tenant_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String tenantId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.tenant_name
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String tenantName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.transformer_capacity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String transformerCapacityPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.max_capacity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String maxCapacityPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.consumption_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String consumptionPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.distribution_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String distributionPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.gov_add_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String govAddPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.sharp_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String sharpPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.peak_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String peakPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.level_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String levelPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.valley_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private String valleyPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.state
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.create_time
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price.update_time
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.id
     *
     * @return the value of electricity_price.id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.id
     *
     * @param id the value for electricity_price.id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.detail_id
     *
     * @return the value of electricity_price.detail_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.detail_id
     *
     * @param detailId the value for electricity_price.detail_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setDetailId(String detailId) {
        this.detailId = detailId == null ? null : detailId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.season_id
     *
     * @return the value of electricity_price.season_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getSeasonId() {
        return seasonId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.season_id
     *
     * @param seasonId the value for electricity_price.season_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId == null ? null : seasonId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.rule_id
     *
     * @return the value of electricity_price.rule_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.rule_id
     *
     * @param ruleId the value for electricity_price.rule_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.tenant_id
     *
     * @return the value of electricity_price.tenant_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.tenant_id
     *
     * @param tenantId the value for electricity_price.tenant_id
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.tenant_name
     *
     * @return the value of electricity_price.tenant_name
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.tenant_name
     *
     * @param tenantName the value for electricity_price.tenant_name
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName == null ? null : tenantName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.transformer_capacity_price
     *
     * @return the value of electricity_price.transformer_capacity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getTransformerCapacityPrice() {
        return transformerCapacityPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.transformer_capacity_price
     *
     * @param transformerCapacityPrice the value for electricity_price.transformer_capacity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setTransformerCapacityPrice(String transformerCapacityPrice) {
        this.transformerCapacityPrice = transformerCapacityPrice == null ? null : transformerCapacityPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.max_capacity_price
     *
     * @return the value of electricity_price.max_capacity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getMaxCapacityPrice() {
        return maxCapacityPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.max_capacity_price
     *
     * @param maxCapacityPrice the value for electricity_price.max_capacity_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setMaxCapacityPrice(String maxCapacityPrice) {
        this.maxCapacityPrice = maxCapacityPrice == null ? null : maxCapacityPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.consumption_price
     *
     * @return the value of electricity_price.consumption_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getConsumptionPrice() {
        return consumptionPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.consumption_price
     *
     * @param consumptionPrice the value for electricity_price.consumption_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setConsumptionPrice(String consumptionPrice) {
        this.consumptionPrice = consumptionPrice == null ? null : consumptionPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.distribution_price
     *
     * @return the value of electricity_price.distribution_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getDistributionPrice() {
        return distributionPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.distribution_price
     *
     * @param distributionPrice the value for electricity_price.distribution_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setDistributionPrice(String distributionPrice) {
        this.distributionPrice = distributionPrice == null ? null : distributionPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.gov_add_price
     *
     * @return the value of electricity_price.gov_add_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getGovAddPrice() {
        return govAddPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.gov_add_price
     *
     * @param govAddPrice the value for electricity_price.gov_add_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setGovAddPrice(String govAddPrice) {
        this.govAddPrice = govAddPrice == null ? null : govAddPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.sharp_price
     *
     * @return the value of electricity_price.sharp_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getSharpPrice() {
        return sharpPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.sharp_price
     *
     * @param sharpPrice the value for electricity_price.sharp_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setSharpPrice(String sharpPrice) {
        this.sharpPrice = sharpPrice == null ? null : sharpPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.peak_price
     *
     * @return the value of electricity_price.peak_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getPeakPrice() {
        return peakPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.peak_price
     *
     * @param peakPrice the value for electricity_price.peak_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setPeakPrice(String peakPrice) {
        this.peakPrice = peakPrice == null ? null : peakPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.level_price
     *
     * @return the value of electricity_price.level_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getLevelPrice() {
        return levelPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.level_price
     *
     * @param levelPrice the value for electricity_price.level_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setLevelPrice(String levelPrice) {
        this.levelPrice = levelPrice == null ? null : levelPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.valley_price
     *
     * @return the value of electricity_price.valley_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public String getValleyPrice() {
        return valleyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.valley_price
     *
     * @param valleyPrice the value for electricity_price.valley_price
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setValleyPrice(String valleyPrice) {
        this.valleyPrice = valleyPrice == null ? null : valleyPrice.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.state
     *
     * @return the value of electricity_price.state
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.state
     *
     * @param state the value for electricity_price.state
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.create_time
     *
     * @return the value of electricity_price.create_time
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.create_time
     *
     * @param createTime the value for electricity_price.create_time
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price.update_time
     *
     * @return the value of electricity_price.update_time
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price.update_time
     *
     * @param updateTime the value for electricity_price.update_time
     *
     * @mbg.generated Fri Apr 29 17:19:47 CST 2022
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}