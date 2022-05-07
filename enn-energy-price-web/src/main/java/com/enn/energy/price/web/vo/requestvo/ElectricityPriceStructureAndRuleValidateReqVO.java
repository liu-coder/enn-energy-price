package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/5
 **/
public class ElectricityPriceStructureAndRuleValidateReqVO implements Serializable {

    private static final long serialVersionUID = -6673586847756569260L;

    @ApiModelProperty(value = "版本id", required = false, dataType = "string")
    private String versionId;

    @ApiModelProperty(value = "体系id", required = false, dataType = "string")
    private String structureId;

    @ApiModelProperty(value = "省编码code", required = true, dataType = "string")
    @NotBlank(message = "省编码code不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "版本生效期", required = true, dataType = "date")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Valid
    private ValidationList<ElectricityPriceStructureRuleValidateReqVO> priceStructureRuleValidateReqVOList;

    @NotEmpty(message = "电价规则不能为空")
    @Valid
    private ValidationList<ElectricityPriceRuleValidateReqVO> priceRuleValidateReqVOList;

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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ValidationList<ElectricityPriceStructureRuleValidateReqVO> getPriceStructureRuleValidateReqVOList() {
        return priceStructureRuleValidateReqVOList;
    }

    public void setPriceStructureRuleValidateReqVOList(ValidationList<ElectricityPriceStructureRuleValidateReqVO> priceStructureRuleValidateReqVOList) {
        this.priceStructureRuleValidateReqVOList = priceStructureRuleValidateReqVOList;
    }

    public ValidationList<ElectricityPriceRuleValidateReqVO> getPriceRuleValidateReqVOList() {
        return priceRuleValidateReqVOList;
    }

    public void setPriceRuleValidateReqVOList(ValidationList<ElectricityPriceRuleValidateReqVO> priceRuleValidateReqVOList) {
        this.priceRuleValidateReqVOList = priceRuleValidateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureAndRuleValidateReqVO{" +
                "versionId='" + versionId + '\'' +
                ", structureId='" + structureId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", startDate=" + startDate +
                ", priceStructureRuleValidateReqVOList=" + priceStructureRuleValidateReqVOList +
                ", priceRuleValidateReqVOList=" + priceRuleValidateReqVOList +
                '}';
    }
}