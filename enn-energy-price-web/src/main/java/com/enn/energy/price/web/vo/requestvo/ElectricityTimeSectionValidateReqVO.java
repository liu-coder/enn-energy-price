package com.enn.energy.price.web.vo.requestvo;

import cn.hutool.core.date.DatePattern;
import com.enn.energy.price.web.validator.DateValue;
import com.enn.energy.price.web.validator.DecimalTemperatureValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 分时区间请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("分时区间请求VO")
public class ElectricityTimeSectionValidateReqVO implements Serializable {

    private static final long serialVersionUID = -8679059488910647014L;

    @ApiModelProperty(value = "开始时间", required = true, dataType = "string")
    @NotBlank(message = "开始时间不能为空")
    @DateValue(format = DatePattern.NORM_TIME_PATTERN, message = "请求时间格式有误")
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true, dataType = "string")
    @NotBlank(message = "结束时间不能为空")
    @DateValue(format = DatePattern.NORM_TIME_PATTERN, message = "请求时间格式有误")
    private String endTime;

    @ApiModelProperty(value = "时段，0:尖;1:峰;2:平;3:谷", required = true, dataType = "string")
    @NotBlank(message = "时段不能为空")
    private String periods;

    @ApiModelProperty(value = "0:等于;-1:小于;1:大于;-10:小于等于;10:大于等于", required = true, dataType = "string")
    private String compare;

    @ApiModelProperty(value = "温度:单位摄氏度", required = true, dataType = "string")
    @DecimalTemperatureValue(message = "温度格式异常")
    private String temperature;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "ElectricityTimeSectionReqVO{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", periods='" + periods + '\'' +
                ", compare='" + compare + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}