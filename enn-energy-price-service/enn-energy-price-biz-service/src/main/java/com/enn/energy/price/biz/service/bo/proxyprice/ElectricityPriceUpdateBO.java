package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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
    private String priceId;
    private String ruleId;
    private Integer ruleChangeType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricityPriceUpdateBO that = (ElectricityPriceUpdateBO) o;
        return getIndustry().equals(that.getIndustry()) && getStrategy().equals(that.getStrategy()) && getVoltageLevel().equals(that.getVoltageLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndustry(), getStrategy(), getVoltageLevel());
    }
}
