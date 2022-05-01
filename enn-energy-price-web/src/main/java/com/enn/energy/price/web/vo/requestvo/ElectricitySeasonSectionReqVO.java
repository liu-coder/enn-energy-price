package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("季节区间请求VO")
public class ElectricitySeasonSectionReqVO implements Serializable {

    private static final long serialVersionUID = 175221351557008274L;

    @ApiModelProperty(value = "开始日期", required = true, dataType = "string")
    @NotBlank(message = "开始日期不能为空,格式MM-dd")
    private String seaStartDate;

    @ApiModelProperty(value = "结束日期", required = true, dataType = "string")
    @NotBlank(message = "结束日期不能为空,格式MM-dd")
    private String seaEndDate;

    @Valid
    private ValidationList<ElectricityTimeSectionReqVO> timeSectionReqVOList;

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

    public ValidationList<ElectricityTimeSectionReqVO> getTimeSectionReqVOList() {
        return timeSectionReqVOList;
    }

    public void setTimeSectionReqVOList(ValidationList<ElectricityTimeSectionReqVO> timeSectionReqVOList) {
        this.timeSectionReqVOList = timeSectionReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonSectionReqVO{" +
                "seaStartDate='" + seaStartDate + '\'' +
                ", seaEndDate='" + seaEndDate + '\'' +
                ", timeSectionReqVOList=" + timeSectionReqVOList +
                '}';
    }
}