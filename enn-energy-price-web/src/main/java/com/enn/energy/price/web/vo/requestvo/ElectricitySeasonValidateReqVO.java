package com.enn.energy.price.web.vo.requestvo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private Date startDate;

    @ApiModelProperty(value = "季节名称", required = true, dataType = "string")
    @NotBlank(message = "季节名称不能为空")
    private String seasonSectionName;

    @NotEmpty(message = "季节区间不能为空")
    private List<@Valid SeasonDateVO> seasonSectionValidateReqVOList;

    private List<@Valid ElectricityPriceStrategyReqVO> timeSectionValidateReqVOList;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSeasonSectionName() {
        return seasonSectionName;
    }

    public void setSeasonSectionName(String seasonSectionName) {
        this.seasonSectionName = seasonSectionName;
    }

    public List<SeasonDateVO> getSeasonSectionValidateReqVOList() {
        return seasonSectionValidateReqVOList;
    }

    public void setSeasonSectionValidateReqVOList(List<SeasonDateVO> seasonSectionValidateReqVOList) {
        this.seasonSectionValidateReqVOList = seasonSectionValidateReqVOList;
    }

    public List<ElectricityPriceStrategyReqVO> getTimeSectionValidateReqVOList() {
        return timeSectionValidateReqVOList;
    }

    public void setTimeSectionValidateReqVOList(List<ElectricityPriceStrategyReqVO> timeSectionValidateReqVOList) {
        this.timeSectionValidateReqVOList = timeSectionValidateReqVOList;
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