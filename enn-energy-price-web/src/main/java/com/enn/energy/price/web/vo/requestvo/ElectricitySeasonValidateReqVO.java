package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
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
public class ElectricitySeasonValidateReqVO implements Serializable {

    private static final long serialVersionUID = -4213396806536606042L;

    @ApiModelProperty(value = "季节名称", required = true, dataType = "string")
    @NotBlank(message = "季节名称不能为空")
    private String seasonSectionName;

    @NotEmpty(message = "季节区间不能为空")
    @Valid
    private ValidationList<ElectricitySeasonSectionValidateReqVO> seasonSectionValidateReqVOList;

    @Valid
    private ValidationList<ElectricityTimeSectionValidateReqVO> timeSectionValidateReqVOList;

    public String getSeasonSectionName() {
        return seasonSectionName;
    }

    public void setSeasonSectionName(String seasonSectionName) {
        this.seasonSectionName = seasonSectionName;
    }

    public ValidationList<ElectricitySeasonSectionValidateReqVO> getSeasonSectionValidateReqVOList() {
        return seasonSectionValidateReqVOList;
    }

    public void setSeasonSectionValidateReqVOList(ValidationList<ElectricitySeasonSectionValidateReqVO> seasonSectionValidateReqVOList) {
        this.seasonSectionValidateReqVOList = seasonSectionValidateReqVOList;
    }

    public ValidationList<ElectricityTimeSectionValidateReqVO> getTimeSectionValidateReqVOList() {
        return timeSectionValidateReqVOList;
    }

    public void setTimeSectionValidateReqVOList(ValidationList<ElectricityTimeSectionValidateReqVO> timeSectionValidateReqVOList) {
        this.timeSectionValidateReqVOList = timeSectionValidateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonValidateReqVO{" +
                "seasonSectionName='" + seasonSectionName + '\'' +
                ", seasonSectionValidateReqVOList=" + seasonSectionValidateReqVOList +
                ", timeSectionValidateReqVOList=" + timeSectionValidateReqVOList +
                '}';
    }
}