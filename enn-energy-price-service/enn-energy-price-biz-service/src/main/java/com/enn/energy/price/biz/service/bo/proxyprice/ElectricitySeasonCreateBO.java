package com.enn.energy.price.biz.service.bo.proxyprice;

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
 * 季节名称BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricitySeasonCreateBO implements Serializable {

    private static final long serialVersionUID = -4213396806536606042L;

    private String seasonSectionName;

    private Date startDate;

    private List<SeasonDateBO> seasonSectionCreateBOList;

    private List<ElectricityPriceStrategyBO> timeSectionCreateBOList;

    public String getSeasonSectionName() {
        return seasonSectionName;
    }

    public void setSeasonSectionName(String seasonSectionName) {
        this.seasonSectionName = seasonSectionName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<SeasonDateBO> getSeasonSectionCreateBOList() {
        return seasonSectionCreateBOList;
    }

    public void setSeasonSectionCreateBOList(List<SeasonDateBO> seasonSectionCreateBOList) {
        this.seasonSectionCreateBOList = seasonSectionCreateBOList;
    }

    public List<ElectricityPriceStrategyBO> getTimeSectionCreateBOList() {
        return timeSectionCreateBOList;
    }

    public void setTimeSectionCreateBOList(List<ElectricityPriceStrategyBO> timeSectionCreateBOList) {
        this.timeSectionCreateBOList = timeSectionCreateBOList;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonCreateBO{" +
                "seasonSectionName='" + seasonSectionName + '\'' +
                ", startDate=" + startDate +
                ", seasonSectionCreateBOList=" + seasonSectionCreateBOList +
                ", timeSectionCreateBOList=" + timeSectionCreateBOList +
                '}';
    }
}