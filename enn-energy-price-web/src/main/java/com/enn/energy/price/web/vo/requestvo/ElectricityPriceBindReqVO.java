package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @description:代理电价绑定请求对象
 * @author:quyl
 * @createTime:2022/5/4 9:07
 */
@ApiModel(description = "代理电价绑定/修改 请求对象")
public class ElectricityPriceBindReqVO implements Serializable {

    @ApiModelProperty(required = true, name = "设备绑定id，编辑室不为空")
    private Long id;

    @ApiModelProperty(required = true, name = "节点Id")
    @NotBlank(message = "节点Id不能为空")
    private String nodeId;

    @ApiModelProperty(required = true, name = "版本ID")
    @NotBlank(message = "版本ID不能为空")
    private String versionId;

    @ApiModelProperty(required = true, name = "体系ID")
    @NotBlank(message = "体系ID不能为空")
    private String structureId;

    @ApiModelProperty(required = true, name = "规则Id")
    @NotBlank(message = "规则Id不能为空")
    private String ruleId;

    @ApiModelProperty(value = "考核功率调整是否开启", example = "0:解绑，1:绑定")
    @NotBlank(message = "考核功率调整是否开启必填")
    @Max(value = 1, message = "考核功率调整是否开启参数只能为0:解绑，1:绑定")
    @Min(value = 0, message = "考核功率调整是否开启参数只能为0:解绑，1:绑定")
    private Integer adjust;

    @ApiModelProperty(value = "是否要自定义下一版本体系", example = "0:否 默认继承，1:是 自定义")
    @NotBlank(message = "是否要自定义下一版本体系标识必填")
    @Max(value = 1, message = "是否要自定义下一版本体系参数只能为0:否 默认继承，1:是 自定义")
    @Min(value = 0, message = "是否要自定义下一版本体系参数只能为0:否 默认继承，1:是 自定义")
    private Integer nextChangeFlag;

    @ApiModelProperty(value = "功率因数", required = true)
    @NotBlank(message = "功率因数必填")
    @Pattern(regexp = "^([0-9]+[.]?[0-9]*)$", message = "功率因数必须为数字")
    @Length(max = 20, message = "功率因数长度不能超过20")
    private String powerFactor;

    @ApiModelProperty(value = "系统编码", required = true)
    @NotBlank(message = "系统编码必填")
    @Length(max = 20, message = "系统编码长度不能超过20")
    private String systemCode;

    @ApiModelProperty(value = "系统名称", required = true)
    @NotBlank(message = "系统名称必填")
    @Length(max = 20, message = "系统名称长度不能超过20")
    private String systemName;

    @ApiModelProperty(value = "企业租户id", required = true)
    @NotBlank(message = "企业租户id必填")
    @Length(max = 20, message = "企业租户id长度不能超过20")
    private String tenantId;

    @ApiModelProperty(value = "企业租户名称", required = true)
    @NotBlank(message = "企业租户名称必填")
    @Length(max = 20, message = "企业租户名称长度不能超过20")
    private String tenantName;

    @ApiModelProperty(value = "选择不继承（自定义）后，下一个版本的体系及价格")
    private NextVersionStructurePrice nextVersionStructurePrice;

    @ApiModel(value = "代理电价绑定时候下一版本选择不继承（自定义）体系电价对象，不继承时候必填")
    public static class NextVersionStructurePrice implements Serializable {

        @ApiModelProperty(required = true, name = "设备绑定id，编辑时不为空")
        private Long id;

        @ApiModelProperty(required = true, name = "版本ID")
        private String versionId;

        @ApiModelProperty(required = true, name = "体系ID")
        private String structureId;

        @ApiModelProperty(required = true, name = "规则Id")
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
            return "NextVersionStructurePrice{" +
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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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

    public Integer getAdjust() {
        return adjust;
    }

    public void setAdjust(Integer adjust) {
        this.adjust = adjust;
    }

    public Integer getNextChangeFlag() {
        return nextChangeFlag;
    }

    public void setNextChangeFlag(Integer nextChangeFlag) {
        this.nextChangeFlag = nextChangeFlag;
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

    public NextVersionStructurePrice getNextVersionStructurePrice() {
        return nextVersionStructurePrice;
    }

    public void setNextVersionStructurePrice(NextVersionStructurePrice nextVersionStructurePrice) {
        this.nextVersionStructurePrice = nextVersionStructurePrice;
    }

    @Override
    public String toString() {
        return "ElectricityPriceBindReqVO{" +
                "id=" + id +
                ", nodeId='" + nodeId + '\'' +
                ", versionId='" + versionId + '\'' +
                ", structureId='" + structureId + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", adjust=" + adjust +
                ", nextChangeFlag=" + nextChangeFlag +
                ", powerFactor='" + powerFactor + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", systemName='" + systemName + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", nextVersionStructurePrice=" + nextVersionStructurePrice +
                '}';
    }
}

