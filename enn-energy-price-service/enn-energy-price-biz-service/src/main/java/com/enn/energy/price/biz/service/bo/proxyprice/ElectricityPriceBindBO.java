package com.enn.energy.price.biz.service.bo.proxyprice;

import java.io.Serializable;

/**
 * @description:电价绑定请求参数
 * @author:quyl
 * @createTime:2022/5/4 15:17
 */
public class ElectricityPriceBindBO implements Serializable {

    private static final long serialVersionUID = -2924062126843990278L;

    private Long id;

    private String versionId;

    private String structureId;

    private String ruleId;

    private String nodeId;

    private Byte adjust;

    private String powerFactor;

    private String systemCode;

    private String systemName;

    private String tenantId;

    private String tenantName;

    private Byte nextChangeFlag;

    /**
     * 编辑or新增，使用ChangeTypeEum 0:add 1:delete 2:update
     */
    private Integer changeType;

    private NextVersionStructurePriceBO nextVersionStructurePriceBO;

    public static class NextVersionStructurePriceBO {

        private Long id;

        private String versionId;

        private String structureId;

        private String ruleId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getStructureId() {
            return structureId;
        }

        public void setStructureId(String structureId) {
            this.structureId = structureId;
        }

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        @Override
        public String toString() {
            return "NextVersionStructurePriceBO{" +
                    "id=" + id +
                    ", versionId='" + versionId + '\'' +
                    ", structureId='" + structureId + '\'' +
                    ", ruleId='" + ruleId + '\'' +
                    '}';
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Byte getAdjust() {
        return adjust;
    }

    public void setAdjust(Byte adjust) {
        this.adjust = adjust;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
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

    public Byte getNextChangeFlag() {
        return nextChangeFlag;
    }

    public void setNextChangeFlag(Byte nextChangeFlag) {
        this.nextChangeFlag = nextChangeFlag;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public NextVersionStructurePriceBO getNextVersionStructurePriceBO() {
        return nextVersionStructurePriceBO;
    }

    public void setNextVersionStructurePriceBO(NextVersionStructurePriceBO nextVersionStructurePriceBO) {
        this.nextVersionStructurePriceBO = nextVersionStructurePriceBO;
    }

    @Override
    public String toString() {
        return "ElectricityPriceBindBO{" +
                "id=" + id +
                ", versionId='" + versionId + '\'' +
                ", structureId='" + structureId + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", adjust=" + adjust +
                ", powerFactor='" + powerFactor + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", systemName='" + systemName + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", nextChangeFlag=" + nextChangeFlag +
                ", changeType=" + changeType +
                ", nextVersionStructurePriceBO=" + nextVersionStructurePriceBO +
                '}';
    }
}
