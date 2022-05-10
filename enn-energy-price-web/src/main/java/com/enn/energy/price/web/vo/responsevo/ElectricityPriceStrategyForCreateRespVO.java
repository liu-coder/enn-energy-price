package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:56
 */
public class ElectricityPriceStrategyForCreateRespVO implements Serializable {

    private static final long serialVersionUID = 8940826543752242135L;

    private String compare;

    private String temperature;

    private List<ElectricityTimeSectionForCreateRespVO> timeSectionForCreateRespVOList;

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

    public List<ElectricityTimeSectionForCreateRespVO> getTimeSectionForCreateRespVOList() {
        return timeSectionForCreateRespVOList;
    }

    public void setTimeSectionForCreateRespVOList(List<ElectricityTimeSectionForCreateRespVO> timeSectionForCreateRespVOList) {
        this.timeSectionForCreateRespVOList = timeSectionForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStrategyForCreateRespVO{" +
                "compare='" + compare + '\'' +
                ", temperature='" + temperature + '\'' +
                ", timeSectionForCreateRespVOList=" + timeSectionForCreateRespVOList +
                '}';
    }
}
