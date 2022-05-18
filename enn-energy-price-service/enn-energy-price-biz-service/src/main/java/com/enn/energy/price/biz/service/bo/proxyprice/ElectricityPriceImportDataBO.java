package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 11:23
 */
public class ElectricityPriceImportDataBO implements Serializable {

    private static final long serialVersionUID = -5369385678138081109L;

    private String provinceCode;

    @NotEmpty
     private List<ElectricityPriceUpdateBO> updateReqVOList;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public List<ElectricityPriceUpdateBO> getUpdateReqVOList() {
        return updateReqVOList;
    }

    public void setUpdateReqVOList(List<ElectricityPriceUpdateBO> updateReqVOList) {
        this.updateReqVOList = updateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceImportDataBO{" +
                "provinceCode='" + provinceCode + '\'' +
                ", updateReqVOList=" + updateReqVOList +
                '}';
    }
}
