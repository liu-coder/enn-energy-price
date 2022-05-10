package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:50
 */
public class ElectricityPriceStructureDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -9163374928956756226L;

    private String structureName;

    private String provinceCode;

    private String cityCodes;

    private String districtCodes;

    private String lastVersionId;

    private String parentid;

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

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
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
                ", parentid='" + parentid + '\'' +
                ", priceDetailForCreateRespVOList=" + priceDetailForCreateRespVOList +
                ", structureRuleDetailForCreateRespVOList=" + structureRuleDetailForCreateRespVOList +
                '}';
    }
}
