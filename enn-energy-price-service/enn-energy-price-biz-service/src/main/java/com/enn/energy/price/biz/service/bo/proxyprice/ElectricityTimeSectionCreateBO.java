package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 分时区间BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityTimeSectionCreateBO implements Serializable {

    private static final long serialVersionUID = -8679059488910647014L;

    private String startTime;

    private String endTime;

    private String periods;

    private String compare;

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