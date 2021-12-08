package com.enn.energy.price.dal.po.mbg;

import java.util.Date;

public class ElectricityPriceDetail {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.detail_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String detailId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.season_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String seasonId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.version_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String versionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.rule_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String ruleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.periods
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String periods;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.start_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String startTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.end_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String endTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String step;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.start_step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String startStep;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.end_step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String endStep;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private String price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.state
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.create_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_detail.update_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.id
     *
     * @return the value of electricity_price_detail.id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.id
     *
     * @param id the value for electricity_price_detail.id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.detail_id
     *
     * @return the value of electricity_price_detail.detail_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.detail_id
     *
     * @param detailId the value for electricity_price_detail.detail_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setDetailId(String detailId) {
        this.detailId = detailId == null ? null : detailId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.season_id
     *
     * @return the value of electricity_price_detail.season_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getSeasonId() {
        return seasonId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.season_id
     *
     * @param seasonId the value for electricity_price_detail.season_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId == null ? null : seasonId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.version_id
     *
     * @return the value of electricity_price_detail.version_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.version_id
     *
     * @param versionId the value for electricity_price_detail.version_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.rule_id
     *
     * @return the value of electricity_price_detail.rule_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.rule_id
     *
     * @param ruleId the value for electricity_price_detail.rule_id
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.periods
     *
     * @return the value of electricity_price_detail.periods
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getPeriods() {
        return periods;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.periods
     *
     * @param periods the value for electricity_price_detail.periods
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setPeriods(String periods) {
        this.periods = periods == null ? null : periods.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.start_time
     *
     * @return the value of electricity_price_detail.start_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.start_time
     *
     * @param startTime the value for electricity_price_detail.start_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.end_time
     *
     * @return the value of electricity_price_detail.end_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.end_time
     *
     * @param endTime the value for electricity_price_detail.end_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.step
     *
     * @return the value of electricity_price_detail.step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getStep() {
        return step;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.step
     *
     * @param step the value for electricity_price_detail.step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setStep(String step) {
        this.step = step == null ? null : step.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.start_step
     *
     * @return the value of electricity_price_detail.start_step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getStartStep() {
        return startStep;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.start_step
     *
     * @param startStep the value for electricity_price_detail.start_step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setStartStep(String startStep) {
        this.startStep = startStep == null ? null : startStep.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.end_step
     *
     * @return the value of electricity_price_detail.end_step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getEndStep() {
        return endStep;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.end_step
     *
     * @param endStep the value for electricity_price_detail.end_step
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setEndStep(String endStep) {
        this.endStep = endStep == null ? null : endStep.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.price
     *
     * @return the value of electricity_price_detail.price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public String getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.price
     *
     * @param price the value for electricity_price_detail.price
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.state
     *
     * @return the value of electricity_price_detail.state
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.state
     *
     * @param state the value for electricity_price_detail.state
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.create_time
     *
     * @return the value of electricity_price_detail.create_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.create_time
     *
     * @param createTime the value for electricity_price_detail.create_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_detail.update_time
     *
     * @return the value of electricity_price_detail.update_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_detail.update_time
     *
     * @param updateTime the value for electricity_price_detail.update_time
     *
     * @mbg.generated Mon Dec 06 17:25:33 CST 2021
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}