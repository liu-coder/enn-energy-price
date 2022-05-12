package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 季节分时对应的体系BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityPriceStructureRuleCreateBO implements Serializable {

    private static final long serialVersionUID = -5758432421217592203L;

    private Long id;

    private String structureId;

    private String structureRuleId;

    private String industries;

    private String strategies;

    private String voltageLevels;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureRuleId() {
        return structureRuleId;
    }

    public void setStructureRuleId(String structureRuleId) {
        this.structureRuleId = structureRuleId;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureRuleCreateBO{" +
                "id=" + id +
                ", structureId='" + structureId + '\'' +
                ", structureRuleId='" + structureRuleId + '\'' +
                ", industries='" + industries + '\'' +
                ", strategies='" + strategies + '\'' +
                ", voltageLevels='" + voltageLevels + '\'' +
                ", seasonCreateBOList=" + seasonCreateBOList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricityPriceStructureRuleCreateBO that = (ElectricityPriceStructureRuleCreateBO) o;
        return getIndustries().equals(that.getIndustries()) && getStrategies().equals(that.getStrategies()) && getVoltageLevels().equals(that.getVoltageLevels());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndustries(), getStrategies(), getVoltageLevels());
    }
}