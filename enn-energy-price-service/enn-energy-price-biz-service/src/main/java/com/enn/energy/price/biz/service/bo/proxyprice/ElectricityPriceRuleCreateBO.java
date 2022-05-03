package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 电价规则BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityPriceRuleCreateBO implements Serializable {

    private static final long serialVersionUID = -583011369969060471L;

    private String industry;

    private String strategy;

    private String voltageLevel;

    private String transformerCapacityPrice;

    private String maxCapacityPrice;

    private ElectricityPriceCreateBO electricityPriceCreateBO;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public String getTransformerCapacityPrice() {
        return transformerCapacityPrice;
    }

    public void setTransformerCapacityPrice(String transformerCapacityPrice) {
        this.transformerCapacityPrice = transformerCapacityPrice;
    }

    public String getMaxCapacityPrice() {
        return maxCapacityPrice;
    }

    public void setMaxCapacityPrice(String maxCapacityPrice) {
        this.maxCapacityPrice = maxCapacityPrice;
    }

    public ElectricityPriceCreateBO getElectricityPriceCreateBO() {
        return electricityPriceCreateBO;
    }

    public void setElectricityPriceCreateBO(ElectricityPriceCreateBO electricityPriceCreateBO) {
        this.electricityPriceCreateBO = electricityPriceCreateBO;
    }

    @Override
    public String toString() {
        return "ElectricityPriceRuleCreateBO{" +
                "industry='" + industry + '\'' +
                ", strategy='" + strategy + '\'' +
                ", voltageLevel='" + voltageLevel + '\'' +
                ", transformerCapacityPrice='" + transformerCapacityPrice + '\'' +
                ", maxCapacityPrice='" + maxCapacityPrice + '\'' +
                ", electricityPriceCreateBO=" + electricityPriceCreateBO +
                '}';
    }
}