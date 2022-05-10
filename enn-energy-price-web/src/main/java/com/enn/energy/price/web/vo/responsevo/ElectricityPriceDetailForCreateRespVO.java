package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:51
 */
public class ElectricityPriceDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -1712746553229580559L;

    private String industry;

    private String strategy;

    private String voltageLevel;

    private String distributionPrice;

    private String govAddPrice;

    private String sharpPrice;

    private String peakPrice;

    private String levelPrice;

    private String valleyPrice;

    private String maxCapacityPrice;

    private String transformerCapacityPrice;

    private String structureRuleId;

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

    public String getDistributionPrice() {
        return distributionPrice;
    }

    public void setDistributionPrice(String distributionPrice) {
        this.distributionPrice = distributionPrice;
    }

    public String getGovAddPrice() {
        return govAddPrice;
    }

    public void setGovAddPrice(String govAddPrice) {
        this.govAddPrice = govAddPrice;
    }

    public String getSharpPrice() {
        return sharpPrice;
    }

    public void setSharpPrice(String sharpPrice) {
        this.sharpPrice = sharpPrice;
    }

    public String getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(String peakPrice) {
        this.peakPrice = peakPrice;
    }

    public String getLevelPrice() {
        return levelPrice;
    }

    public void setLevelPrice(String levelPrice) {
        this.levelPrice = levelPrice;
    }

    public String getValleyPrice() {
        return valleyPrice;
    }

    public void setValleyPrice(String valleyPrice) {
        this.valleyPrice = valleyPrice;
    }

    public String getMaxCapacityPrice() {
        return maxCapacityPrice;
    }

    public void setMaxCapacityPrice(String maxCapacityPrice) {
        this.maxCapacityPrice = maxCapacityPrice;
    }

    public String getTransformerCapacityPrice() {
        return transformerCapacityPrice;
    }

    public void setTransformerCapacityPrice(String transformerCapacityPrice) {
        this.transformerCapacityPrice = transformerCapacityPrice;
    }

    public String getStructureRuleId() {
        return structureRuleId;
    }

    public void setStructureRuleId(String structureRuleId) {
        this.structureRuleId = structureRuleId;
    }

    @Override
    public String toString() {
        return "ElectricityPriceDetailForCreateRespVO{" +
                "industry='" + industry + '\'' +
                ", strategy='" + strategy + '\'' +
                ", voltageLevel='" + voltageLevel + '\'' +
                ", distributionPrice='" + distributionPrice + '\'' +
                ", govAddPrice='" + govAddPrice + '\'' +
                ", sharpPrice='" + sharpPrice + '\'' +
                ", peakPrice='" + peakPrice + '\'' +
                ", levelPrice='" + levelPrice + '\'' +
                ", valleyPrice='" + valleyPrice + '\'' +
                ", maxCapacityPrice='" + maxCapacityPrice + '\'' +
                ", transformerCapacityPrice='" + transformerCapacityPrice + '\'' +
                ", structureRuleId='" + structureRuleId + '\'' +
                '}';
    }
}
