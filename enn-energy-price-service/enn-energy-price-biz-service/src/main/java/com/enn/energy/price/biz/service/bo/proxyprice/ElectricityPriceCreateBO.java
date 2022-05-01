package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 规则电价明细BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityPriceCreateBO implements Serializable {

    private static final long serialVersionUID = -5187909940116643626L;

    private String consumptionPrice;

    private String distributionPrice;

    private String govAddPrice;

    private String sharpPrice;

    private String peakPrice;

    private String levelPrice;

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