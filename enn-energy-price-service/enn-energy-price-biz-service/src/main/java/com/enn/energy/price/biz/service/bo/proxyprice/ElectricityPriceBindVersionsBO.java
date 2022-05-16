package com.enn.energy.price.biz.service.bo.proxyprice;

import java.io.Serializable;

/**
 * @description:根据省市区查找当前有效的版本列表请求对象
 * @author:quyl
 * @createTime:2022/5/4 15:17
 */
public class ElectricityPriceBindVersionsBO implements Serializable {

    private String provinceCode;

    private String cityCode;

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
        return "ElectricityPriceBindVersionsBO{" +
                "provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                '}';
    }
}
