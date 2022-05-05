package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
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
