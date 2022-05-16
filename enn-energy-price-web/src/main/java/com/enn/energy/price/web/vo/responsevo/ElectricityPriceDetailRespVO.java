package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceDetailRespVO implements Serializable {
    @ApiModelProperty(value = "规则id")
    private String ruleId;
    @ApiModelProperty(value = "价格id")
    private String priceId;
    @ApiModelProperty(value = "体系规则id")
    private String structureRuleId;
    @ApiModelProperty(value = "用电分类")
    private String industry;
    @ApiModelProperty(value = "定价类型 0:单一制;1:双部制")
    private String strategy;
    @ApiModelProperty(value = "电压等级")
    private String voltageLevel;
    @ApiModelProperty(value = "变压器容量")
    private String transformerCapacityPrice;
    @ApiModelProperty(value = "最大需量")
    private String maxCapacityPrice;
    @ApiModelProperty(value = "电度用电价格")
    private String consumptionPrice;
    @ApiModelProperty(value = "电度输配价格")
    private String distributionPrice;
    @ApiModelProperty(value = "政府附加价格")
    private String govAddPrice;
    @ApiModelProperty(value = "尖价")
    private String sharpPrice;
    @ApiModelProperty(value = "峰价")
    private String peakPrice;
    @ApiModelProperty(value = "平价")
    private String levelPrice;
    @ApiModelProperty(value = "谷价")
    private String valleyPrice;
}
