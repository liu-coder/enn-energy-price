package com.enn.energy.price.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/22 10:35
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceValueDetailRespDTO implements Serializable {
    private String strategy;
    private String pricingMethod;
    private BigDecimal baseCapacityPrice;
    private BigDecimal maxCapacityPrice;
    private List<PriceDetail> priceDetails;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceDetail implements Serializable {
        private String periods;
        private String startTime;
        private String endTime;
        private String step;
        private String startStep;
        private String endStep;
        private BigDecimal elePrice;

    }
}
