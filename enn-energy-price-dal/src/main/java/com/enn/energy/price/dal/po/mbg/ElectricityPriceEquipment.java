package com.enn.energy.price.dal.po.mbg;

import java.util.Date;

public class ElectricityPriceEquipment {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.equipment_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String equipmentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.equipment_name
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String equipmentName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.rule_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String ruleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.version_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String versionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.tenant_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String tenantId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.tenant_name
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String tenantName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.structure_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String structureId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.structure_rule_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String structureRuleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.district_codes
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String districtCodes;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.system_code
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String systemCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.power_factor
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String powerFactor;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.adjust
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private Byte adjust;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.next_change_flag
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private Byte nextChangeFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.state
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.creator
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String creator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.updator
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private String updator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.create_time
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column electricity_price_equipment.update_time
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.id
     *
     * @return the value of electricity_price_equipment.id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.id
     *
     * @param id the value for electricity_price_equipment.id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.equipment_id
     *
     * @return the value of electricity_price_equipment.equipment_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getEquipmentId() {
        return equipmentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.equipment_id
     *
     * @param equipmentId the value for electricity_price_equipment.equipment_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId == null ? null : equipmentId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.equipment_name
     *
     * @return the value of electricity_price_equipment.equipment_name
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getEquipmentName() {
        return equipmentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.equipment_name
     *
     * @param equipmentName the value for electricity_price_equipment.equipment_name
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName == null ? null : equipmentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.rule_id
     *
     * @return the value of electricity_price_equipment.rule_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.rule_id
     *
     * @param ruleId the value for electricity_price_equipment.rule_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.version_id
     *
     * @return the value of electricity_price_equipment.version_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.version_id
     *
     * @param versionId the value for electricity_price_equipment.version_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.tenant_id
     *
     * @return the value of electricity_price_equipment.tenant_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.tenant_id
     *
     * @param tenantId the value for electricity_price_equipment.tenant_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.tenant_name
     *
     * @return the value of electricity_price_equipment.tenant_name
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.tenant_name
     *
     * @param tenantName the value for electricity_price_equipment.tenant_name
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName == null ? null : tenantName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.structure_id
     *
     * @return the value of electricity_price_equipment.structure_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getStructureId() {
        return structureId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.structure_id
     *
     * @param structureId the value for electricity_price_equipment.structure_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setStructureId(String structureId) {
        this.structureId = structureId == null ? null : structureId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.structure_rule_id
     *
     * @return the value of electricity_price_equipment.structure_rule_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getStructureRuleId() {
        return structureRuleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.structure_rule_id
     *
     * @param structureRuleId the value for electricity_price_equipment.structure_rule_id
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setStructureRuleId(String structureRuleId) {
        this.structureRuleId = structureRuleId == null ? null : structureRuleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.district_codes
     *
     * @return the value of electricity_price_equipment.district_codes
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getDistrictCodes() {
        return districtCodes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.district_codes
     *
     * @param districtCodes the value for electricity_price_equipment.district_codes
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setDistrictCodes(String districtCodes) {
        this.districtCodes = districtCodes == null ? null : districtCodes.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.system_code
     *
     * @return the value of electricity_price_equipment.system_code
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getSystemCode() {
        return systemCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.system_code
     *
     * @param systemCode the value for electricity_price_equipment.system_code
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.power_factor
     *
     * @return the value of electricity_price_equipment.power_factor
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getPowerFactor() {
        return powerFactor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.power_factor
     *
     * @param powerFactor the value for electricity_price_equipment.power_factor
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor == null ? null : powerFactor.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.adjust
     *
     * @return the value of electricity_price_equipment.adjust
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public Byte getAdjust() {
        return adjust;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.adjust
     *
     * @param adjust the value for electricity_price_equipment.adjust
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setAdjust(Byte adjust) {
        this.adjust = adjust;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.next_change_flag
     *
     * @return the value of electricity_price_equipment.next_change_flag
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public Byte getNextChangeFlag() {
        return nextChangeFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.next_change_flag
     *
     * @param nextChangeFlag the value for electricity_price_equipment.next_change_flag
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setNextChangeFlag(Byte nextChangeFlag) {
        this.nextChangeFlag = nextChangeFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.state
     *
     * @return the value of electricity_price_equipment.state
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.state
     *
     * @param state the value for electricity_price_equipment.state
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.creator
     *
     * @return the value of electricity_price_equipment.creator
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.creator
     *
     * @param creator the value for electricity_price_equipment.creator
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.updator
     *
     * @return the value of electricity_price_equipment.updator
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public String getUpdator() {
        return updator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.updator
     *
     * @param updator the value for electricity_price_equipment.updator
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.create_time
     *
     * @return the value of electricity_price_equipment.create_time
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.create_time
     *
     * @param createTime the value for electricity_price_equipment.create_time
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column electricity_price_equipment.update_time
     *
     * @return the value of electricity_price_equipment.update_time
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column electricity_price_equipment.update_time
     *
     * @param updateTime the value for electricity_price_equipment.update_time
     *
     * @mbg.generated Tue May 17 09:36:34 CST 2022
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}