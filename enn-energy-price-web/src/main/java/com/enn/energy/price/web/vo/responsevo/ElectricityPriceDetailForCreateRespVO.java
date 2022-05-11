package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("电价详细响应VO")
public class ElectricityPriceDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -1712746553229580559L;

    @ApiModelProperty(value = "用电行业", dataType = "string")
    private String industry;

    @ApiModelProperty(value = "定价策略", dataType = "string")
    private String strategy;

    @ApiModelProperty(value = "电压等级", dataType = "string")
    private String voltageLevel;

    @ApiModelProperty(value = "电度输配价格", dataType = "string")
    private String distributionPrice;

    @ApiModelProperty(value = "政府附加价格", dataType = "string")
    private String govAddPrice;

    @ApiModelProperty(value = "尖价", dataType = "string")
    private String sharpPrice;

    @ApiModelProperty(value = "峰价", dataType = "string")
    private String peakPrice;

    @ApiModelProperty(value = "平价", dataType = "string")
    private String levelPrice;

    @ApiModelProperty(value = "谷价", dataType = "string")
    private String valleyPrice;

    @ApiModelProperty(value = "最大容量基础电价", dataType = "string")
    private String maxCapacityPrice;

    @ApiModelProperty(value = "变压器容量基础电价", dataType = "string")
    private String transformerCapacityPrice;

    @ApiModelProperty(value = "体系id", dataType = "string")
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
