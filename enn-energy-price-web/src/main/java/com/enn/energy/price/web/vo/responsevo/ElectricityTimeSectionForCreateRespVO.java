package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:58
 */
@ApiModel("季节分时对应的体系请求VO")
public class ElectricityTimeSectionForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -8640380650910393474L;

    @ApiModelProperty(value = "时段，0:尖;1:峰;2:平;3:谷", dataType = "string")
    private String periods;

    @ApiModelProperty(value = "开始时间", dataType = "string")
    private String startTime;

    @ApiModelProperty(value = "结束时间", dataType = "string")
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
