package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 季节名称请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("季节名称请求VO")
public class ElectricitySeasonValidateReqVO implements Serializable {

    private static final long serialVersionUID = -4213396806536606042L;

    @ApiModelProperty(value = "版本生效期", required = true, dataType = "date")
    @NotNull(message = "生效日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonValidateReqVO{" +
                "startDate=" + startDate +
                ", seasonSectionName='" + seasonSectionName + '\'' +
                ", seasonSectionValidateReqVOList=" + seasonSectionValidateReqVOList +
                ", timeSectionValidateReqVOList=" + timeSectionValidateReqVOList +
                '}';
    }
}