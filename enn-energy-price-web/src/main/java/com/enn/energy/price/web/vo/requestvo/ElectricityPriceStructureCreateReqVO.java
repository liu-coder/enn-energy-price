package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 电价体系请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("电价体系请求VO")
public class ElectricityPriceStructureCreateReqVO implements Serializable {

    private static final long serialVersionUID = 511905937266179017L;

    @ApiModelProperty(value = "体系名称", required = true, dataType = "string")
    @NotBlank(message = "体系名称不能为空")
    @Length(min = 1, max = 50)
    private String structureName;

    @ApiModelProperty(value = "省编码code", required = true, dataType = "string")
    @NotBlank(message = "省编码code不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "市编码code，以,拼接", required = true, dataType = "string")
    @NotBlank(message = "市编码code不能为空")
    private String cityCodes;

    @ApiModelProperty(value = "区编码code，以,拼接", required = true, dataType = "string")
    @NotBlank(message = "区编码code不能为空")
    private String districtCodes;

    @ApiModelProperty(value = "父体系id", required = true, dataType = "string")
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
        return "ElectricityPriceStructureCreateReqVO{" +
                "structureName='" + structureName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCodes='" + cityCodes + '\'' +
                ", districtCodes='" + districtCodes + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}