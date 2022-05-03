package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 季节名称BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricitySeasonCreateBO implements Serializable {

    private static final long serialVersionUID = -4213396806536606042L;

    private String seasonSectionName;

    private ValidationList<ElectricitySeasonSectionCreateBO> seasonSectionCreateBOList;

    private ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList;

    public String getSeasonSectionName() {
        return seasonSectionName;
    }

    public void setSeasonSectionName(String seasonSectionName) {
        this.seasonSectionName = seasonSectionName;
    }

    public ValidationList<ElectricitySeasonSectionCreateBO> getSeasonSectionCreateBOList() {
        return seasonSectionCreateBOList;
    }

    public void setSeasonSectionCreateBOList(ValidationList<ElectricitySeasonSectionCreateBO> seasonSectionCreateBOList) {
        this.seasonSectionCreateBOList = seasonSectionCreateBOList;
    }

    public ValidationList<ElectricityTimeSectionCreateBO> getTimeSectionCreateBOList() {
        return timeSectionCreateBOList;
    }

    public void setTimeSectionCreateBOList(ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList) {
        this.timeSectionCreateBOList = timeSectionCreateBOList;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonCreateBO{" +
                "seasonSectionName='" + seasonSectionName + '\'' +
                ", seasonSectionCreateBOList=" + seasonSectionCreateBOList +
                ", timeSectionCreateBOList=" + timeSectionCreateBOList +
                '}';
    }
}