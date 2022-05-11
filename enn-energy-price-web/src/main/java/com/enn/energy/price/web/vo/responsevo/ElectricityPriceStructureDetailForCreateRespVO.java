package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:50
 */
@ApiModel("体系以及对应下的体系配置内容")
public class ElectricityPriceStructureDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -9163374928956756226L;

    @ApiModelProperty(value = "体系名称", dataType = "string")
    private String structureName;

    @ApiModelProperty(value = "省编码code", dataType = "string")
    private String provinceCode;

    @ApiModelProperty(value = "市编码code，以,拼接", dataType = "string")
    private String cityCodes;

    @ApiModelProperty(value = "区编码code，以,拼接", dataType = "string")
    private String districtCodes;

    @ApiModelProperty(value = "父版本id", dataType = "string")
    private String lastVersionId;

    @ApiModelProperty(value = "父体系id", dataType = "string")
    private String parentId;

    private List<ElectricityPriceDetailForCreateRespVO> priceDetailForCreateRespVOList;

    private List<ElectricityPriceStructureRuleDetailForCreateRespVO> structureRuleDetailForCreateRespVOList;

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCodes() {
        return cityCodes;
    }

    public void setCityCodes(String cityCodes) {
        this.cityCodes = cityCodes;
    }

    public String getDistrictCodes() {
        return districtCodes;
    }

    public void setDistrictCodes(String districtCodes) {
        this.districtCodes = districtCodes;
    }

    public String getLastVersionId() {
        return lastVersionId;
    }

    public void setLastVersionId(String lastVersionId) {
        this.lastVersionId = lastVersionId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<ElectricityPriceDetailForCreateRespVO> getPriceDetailForCreateRespVOList() {
        return priceDetailForCreateRespVOList;
    }

    public void setPriceDetailForCreateRespVOList(List<ElectricityPriceDetailForCreateRespVO> priceDetailForCreateRespVOList) {
        this.priceDetailForCreateRespVOList = priceDetailForCreateRespVOList;
    }

    public List<ElectricityPriceStructureRuleDetailForCreateRespVO> getStructureRuleDetailForCreateRespVOList() {
        return structureRuleDetailForCreateRespVOList;
    }

    public void setStructureRuleDetailForCreateRespVOList(List<ElectricityPriceStructureRuleDetailForCreateRespVO> structureRuleDetailForCreateRespVOList) {
        this.structureRuleDetailForCreateRespVOList = structureRuleDetailForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureDetailForCreateRespVO{" +
                "structureName='" + structureName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCodes='" + cityCodes + '\'' +
                ", districtCodes='" + districtCodes + '\'' +
                ", lastVersionId='" + lastVersionId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", priceDetailForCreateRespVOList=" + priceDetailForCreateRespVOList +
                ", structureRuleDetailForCreateRespVOList=" + structureRuleDetailForCreateRespVOList +
                '}';
    }
}
