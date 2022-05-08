package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceDetailRespVO implements Serializable {
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
    private String structureRuleId;
}
