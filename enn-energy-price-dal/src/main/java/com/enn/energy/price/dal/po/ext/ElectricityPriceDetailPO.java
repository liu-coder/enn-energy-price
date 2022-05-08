package com.enn.energy.price.dal.po.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 11:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceDetailPO {
    private String ruleId;
    private String priceId;
    private String industry;
    private String strategy;
    private String voltageLevel;
    private BigDecimal distributionPrice;
    private BigDecimal govAddPrice;
    private BigDecimal sharpPrice;
    private BigDecimal peakPrice;
    private BigDecimal levelPrice;
    private BigDecimal valleyPrice;
    private BigDecimal maxCapacityPrice;
    private BigDecimal transformerCapacityPrice;
    private BigDecimal consumptionPrice;
    private String structureRuleId;
}
