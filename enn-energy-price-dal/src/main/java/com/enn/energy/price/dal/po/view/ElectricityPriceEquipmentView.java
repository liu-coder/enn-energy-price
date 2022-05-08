package com.enn.energy.price.dal.po.view;

import java.util.Date;

/**
 * 设备规则绑定关系view
 *
 * @author sunjidong
 * @date 2022/5/5
 **/
public class ElectricityPriceEquipmentView {

    private Long id;

    private String equipmentId;

    private String equipmentName;

    private String ruleId;

    private String industry;

    private String strategy;

    private String voltageLevel;

    private String versionId;

    private String tenantId;

    private String tenantName;

    private String structureId;

    private String structureRuleId;

    private String systemCode;

    private Byte adjust;

    private Integer state;

    private String creator;

    private String updator;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureRuleId() {
        return structureRuleId;
    }

    public void setStructureRuleId(String structureRuleId) {
        this.structureRuleId = structureRuleId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Byte getAdjust() {
        return adjust;
    }

    public void setAdjust(Byte adjust) {
        this.adjust = adjust;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ElectricityPriceEquipmentView{" +
                "id=" + id +
                ", equipmentId='" + equipmentId + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", industry='" + industry + '\'' +
                ", strategy='" + strategy + '\'' +
                ", voltageLevel='" + voltageLevel + '\'' +
                ", versionId='" + versionId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", structureId='" + structureId + '\'' +
                ", structureRuleId='" + structureRuleId + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", adjust=" + adjust +
                ", state=" + state +
                ", creator='" + creator + '\'' +
                ", updator='" + updator + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}