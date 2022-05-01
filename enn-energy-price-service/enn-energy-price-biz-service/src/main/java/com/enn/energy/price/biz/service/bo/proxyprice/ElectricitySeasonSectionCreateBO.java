package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 季节区间BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricitySeasonSectionCreateBO implements Serializable {

    private static final long serialVersionUID = 175221351557008274L;

    private String seaStartDate;

    private String seaEndDate;

    @Valid
    private ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList;

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

    public ValidationList<ElectricityTimeSectionCreateBO> getTimeSectionCreateBOList() {
        return timeSectionCreateBOList;
    }

    public void setTimeSectionCreateBOList(ValidationList<ElectricityTimeSectionCreateBO> timeSectionCreateBOList) {
        this.timeSectionCreateBOList = timeSectionCreateBOList;
    }

    @Override
    public String toString() {
        return "ElectricitySeasonSectionCreateBO{" +
                "seaStartDate='" + seaStartDate + '\'' +
                ", seaEndDate='" + seaEndDate + '\'' +
                ", timeSectionCreateBOList=" + timeSectionCreateBOList +
                '}';
    }
}