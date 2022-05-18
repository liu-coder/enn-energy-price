package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 16:26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityPriceDetailBO implements Serializable {
    private static final long serialVersionUID = 2147510056633368864L;
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
