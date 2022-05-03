package com.enn.energy.price.web.vo.requestvo;

import cn.hutool.core.date.DatePattern;
import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import com.enn.energy.price.web.validator.DateValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("季节区间请求VO")
public class ElectricitySeasonSectionCreateReqVO implements Serializable {

    private static final long serialVersionUID = 175221351557008274L;

    @ApiModelProperty(value = "开始日期", required = true, dataType = "string")
    @NotBlank(message = "开始日期不能为空")
    @DateValue(format = "MM-dd", message = "请求时间格式有误")
    private String seaStartDate;

    @ApiModelProperty(value = "结束日期", required = true, dataType = "string")
    @NotBlank(message = "结束日期不能为空")
    @DateValue(format = "MM-dd", message = "请求时间格式有误")
    private String seaEndDate;

    public String getSeaStartDate() {
        return seaStartDate;
    }

    public void setSeaStartDate(String seaStartDate) {
        this.seaStartDate = seaStartDate;
    }

    public String getSeaEndDate() {
        return seaEndDate;
    }

    public void setSeaEndDate(String seaEndDate) {
        this.seaEndDate = seaEndDate;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonSectionCreateReqVO{" +
                "seaStartDate='" + seaStartDate + '\'' +
                ", seaEndDate='" + seaEndDate + '\'' +
                '}';
    }
}