package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description: 根据省市区查找当前有效的版本列表
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@ApiModel(description = "根据省市区查找当前有效的版本列表请求对象")
public class ElectricityPriceBindVersionsReqVO implements Serializable {

    @ApiModelProperty(required = true, name = "省code")
    @NotNull(message = "省code不能为空")
    private String provinceCode;

    @ApiModelProperty(name = "市code")
    private String cityCode;

    @ApiModelProperty(name = "区code")
    private String districtCode;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @Override
    public String toString() {
        return "ElectricityPriceBindVersionsReqVO{" +
                "provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                '}';
    }
}
