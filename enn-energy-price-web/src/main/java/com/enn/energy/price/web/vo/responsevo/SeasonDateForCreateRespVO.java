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
 * @Date 2022/5/8 17:55
 */@ApiModel("季节区间响应VO")
public class SeasonDateForCreateRespVO implements Serializable {

    private static final long serialVersionUID = 5339595067267620081L;

    @ApiModelProperty(value = "开始日期", dataType = "string")
    private String seaStartDate;

    @ApiModelProperty(value = "结束日期", dataType = "string")
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
        return "SeasonDateForCreateRespVO{" +
                "seaStartDate='" + seaStartDate + '\'' +
                ", seaEndDate='" + seaEndDate + '\'' +
                '}';
    }
}
