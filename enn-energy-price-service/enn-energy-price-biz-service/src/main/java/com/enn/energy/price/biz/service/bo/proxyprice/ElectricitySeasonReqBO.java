package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 季节名称请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("季节名称请求VO")
public class ElectricitySeasonReqBO implements Serializable {

    private static final long serialVersionUID = -4213396806536606042L;

    @ApiModelProperty(value = "季节名称", required = true, dataType = "string")
    @NotBlank(message = "季节名称不能为空")
    private String seasonSectionName;

    @NotEmpty(message = "必须配置季节区间")
    @Valid
    private ValidationList<ElectricitySeasonSectionReqBO> seasonSectionReqVOList;

    public String getSeasonSectionName() {
        return seasonSectionName;
    }

    public void setSeasonSectionName(String seasonSectionName) {
        this.seasonSectionName = seasonSectionName;
    }

    public ValidationList<ElectricitySeasonSectionReqBO> getSeasonSectionReqVOList() {
        return seasonSectionReqVOList;
    }

    public void setSeasonSectionReqVOList(ValidationList<ElectricitySeasonSectionReqBO> seasonSectionReqVOList) {
        this.seasonSectionReqVOList = seasonSectionReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonReqVO{" +
                "seasonSectionName='" + seasonSectionName + '\'' +
                ", seasonSectionReqVOList=" + seasonSectionReqVOList +
                '}';
    }
}