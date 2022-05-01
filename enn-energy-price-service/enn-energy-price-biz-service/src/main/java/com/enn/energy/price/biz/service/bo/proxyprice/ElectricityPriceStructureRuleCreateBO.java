package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 季节分时对应的体系BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityPriceStructureRuleCreateBO implements Serializable {

    private static final long serialVersionUID = -5758432421217592203L;

    private String industries;

    private String strategies;

    private String voltageLevels;

    @NotNull
    @Valid
    private ValidationList<ElectricitySeasonCreateBO> seasonCreateBOList;

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }

    public String getStrategies() {
        return strategies;
    }

    public void setStrategies(String strategies) {
        this.strategies = strategies;
    }

    public String getVoltageLevels() {
        return voltageLevels;
    }

    public void setVoltageLevels(String voltageLevels) {
        this.voltageLevels = voltageLevels;
    }

    public ValidationList<ElectricitySeasonCreateBO> getSeasonCreateBOList() {
        return seasonCreateBOList;
    }

    public void setSeasonCreateBOList(ValidationList<ElectricitySeasonCreateBO> seasonCreateBOList) {
        this.seasonCreateBOList = seasonCreateBOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureRuleCreateBO{" +
                "industries='" + industries + '\'' +
                ", strategies='" + strategies + '\'' +
                ", voltageLevels='" + voltageLevels + '\'' +
                ", seasonCreateBOList=" + seasonCreateBOList +
                '}';
    }
}