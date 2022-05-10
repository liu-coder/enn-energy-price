package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:58
 */
public class ElectricityTimeSectionForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -8640380650910393474L;

    private String periods;

    private String startTime;

    private String endTime;

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

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

    @Override
    public String toString() {
        return "ElectricityTimeSectionForCreateRespVO{" +
                "periods='" + periods + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
