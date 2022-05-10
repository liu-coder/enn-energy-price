package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/5
 **/
public class ElectricityPriceStructureAndRuleValidateBO implements Serializable {

    private static final long serialVersionUID = -6673586847756569260L;

    private String versionId;

    private String structureId;

    private String tenantId;

    private String provinceCode;

    private Date startDate;

    private String versionName;

    private ValidationList<ElectricityPriceStructureRuleCreateBO> priceStructureRuleValidateReqVOList;

    private ValidationList<ElectricityPriceRuleCreateBO> priceRuleValidateReqVOList;

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

    public ValidationList<ElectricityPriceStructureRuleCreateBO> getPriceStructureRuleValidateReqVOList() {
        return priceStructureRuleValidateReqVOList;
    }

    public void setPriceStructureRuleValidateReqVOList(ValidationList<ElectricityPriceStructureRuleCreateBO> priceStructureRuleValidateReqVOList) {
        this.priceStructureRuleValidateReqVOList = priceStructureRuleValidateReqVOList;
    }

    public ValidationList<ElectricityPriceRuleCreateBO> getPriceRuleValidateReqVOList() {
        return priceRuleValidateReqVOList;
    }

    public void setPriceRuleValidateReqVOList(ValidationList<ElectricityPriceRuleCreateBO> priceRuleValidateReqVOList) {
        this.priceRuleValidateReqVOList = priceRuleValidateReqVOList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureAndRuleValidateBO{" +
                "versionId='" + versionId + '\'' +
                ", structureId='" + structureId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", startDate=" + startDate +
                ", versionName='" + versionName + '\'' +
                ", priceStructureRuleValidateReqVOList=" + priceStructureRuleValidateReqVOList +
                ", priceRuleValidateReqVOList=" + priceRuleValidateReqVOList +
                '}';
    }
}