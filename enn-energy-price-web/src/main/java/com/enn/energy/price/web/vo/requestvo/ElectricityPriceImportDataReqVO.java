package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.web.validator.DecimalValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@ApiModel("电价价格导入请求vo")
public class ElectricityPriceImportDataReqVO implements Serializable {

    private static final long serialVersionUID = -5369385678138081109L;

    @ApiModelProperty(value = "省编码",required = true, dataType = "string")
    @NotBlank(message = "省编码不能为空")
    private String provinceCode;

     private List<@Valid ElectricityPriceUpdateReqVO> updateReqVOList;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public List<ElectricityPriceUpdateReqVO> getUpdateReqVOList() {
        return updateReqVOList;
    }

    public void setUpdateReqVOList(List<ElectricityPriceUpdateReqVO> updateReqVOList) {
        this.updateReqVOList = updateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceImportDataReqVO{" +
                "provinceCode='" + provinceCode + '\'' +
                ", updateReqVOList=" + updateReqVOList +
                '}';
    }
}
