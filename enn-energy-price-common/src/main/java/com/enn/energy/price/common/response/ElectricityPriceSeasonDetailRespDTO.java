package com.enn.energy.price.common.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/12/2 17:18
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ElectricityPriceSeasonDetailRespDTO implements Serializable {
    //季节开始时间#季节结束时间#尖峰平谷开始时间#尖峰平谷结束时间#价格
    private String pricingMethod;
    private List<PriceDetailRespDTO> priceList;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class PriceDetailRespDTO implements Serializable {
        private String startTime;
        private String endTime;
        private String step;
        private String startStep;
        private String endStep;
        private Integer price;
    }
}
