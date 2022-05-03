package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 电价体系BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityPriceStructureCreateBO implements Serializable {

    private static final long serialVersionUID = 511905937266179017L;

    private String structureName;

    private String provinceCode;

    private String cityCodes;

    private String districtCodes;

    private String parentId;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureCreateBO{" +
                "structureName='" + structureName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCodes='" + cityCodes + '\'' +
                ", districtCodes='" + districtCodes + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}