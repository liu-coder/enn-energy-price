package com.enn.energy.price.client.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	@ApiModelProperty(value = "定价策略，0:单一制;1:双部制")
    private String strategy;
	@ApiModelProperty(value = "定价方式，p:单一电价;tp:分时电价;lp:阶梯电价，根据定义方式获取明细不同的字段")
    private String pricingMethod;
	@ApiModelProperty(value = "变压器容量基础电价")
    private BigDecimal baseCapacityPrice;
	@ApiModelProperty(value = "最大容量基础电价")
    private BigDecimal maxCapacityPrice;

	@ApiModelProperty(value = "电价明细")

    private List<PriceDetail> priceDetails;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceDetail implements Serializable {
		@ApiModelProperty(value = "时段，0:尖;1:峰;2:平;3:谷")
        private String periods;
		@ApiModelProperty(value = "开始时间", example = "14:00")
        private String startTime;
		@ApiModelProperty(value = "结束时间", example = "16:00")
        private String endTime;
		@ApiModelProperty(value = "阶梯定义")
        private String step;
		@ApiModelProperty(value = "阶梯起码")
        private String startStep;
		@ApiModelProperty(value = "阶梯止码")
        private String endStep;
		@ApiModelProperty(value = "电价", name = "price")
		@JsonInclude(JsonInclude.Include.ALWAYS)
        private BigDecimal elePrice;

    }
}
