package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceUpdateBO implements Serializable {
    private static final long serialVersionUID = -607570668098849354L;
    private Long priceId;
    private Long ruleId;
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
    private String consumptionPrice;
    private String structureRuleId;
    private Boolean comply;
    private String serialNo;
    private Integer ruleChangeType;
}
