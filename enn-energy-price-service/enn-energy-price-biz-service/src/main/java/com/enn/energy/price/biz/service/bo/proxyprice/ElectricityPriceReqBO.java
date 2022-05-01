package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 规则电价明细请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("规则电价明细请求VO")
public class ElectricityPriceReqBO implements Serializable {

    private static final long serialVersionUID = -5187909940116643626L;

    @ApiModelProperty(value = "电度用电价格", required = false, dataType = "string")
    private String consumptionPrice;

    @ApiModelProperty(value = "电度输配价格", required = false, dataType = "string")
    private String distributionPrice;

    @ApiModelProperty(value = "政府附加价格", required = false, dataType = "string")
    private String govAddPrice;

    @ApiModelProperty(value = "尖价", required = false, dataType = "string")
    private String sharpPrice;

    @ApiModelProperty(value = "峰价", required = false, dataType = "string")
    private String peakPrice;

    @ApiModelProperty(value = "平价", required = false, dataType = "string")
    private String levelPrice;

    @ApiModelProperty(value = "谷价", required = false, dataType = "string")
    private String valleyPrice;

    public String getConsumptionPrice() {
        return consumptionPrice;
    }

    public void setConsumptionPrice(String consumptionPrice) {
        this.consumptionPrice = consumptionPrice;
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

    @Override
    public String toString() {
        return "ElectricityPriceReqVO{" +
                "consumptionPrice='" + consumptionPrice + '\'' +
                ", distributionPrice='" + distributionPrice + '\'' +
                ", govAddPrice='" + govAddPrice + '\'' +
                ", sharpPrice='" + sharpPrice + '\'' +
                ", peakPrice='" + peakPrice + '\'' +
                ", levelPrice='" + levelPrice + '\'' +
                ", valleyPrice='" + valleyPrice + '\'' +
                '}';
    }
}