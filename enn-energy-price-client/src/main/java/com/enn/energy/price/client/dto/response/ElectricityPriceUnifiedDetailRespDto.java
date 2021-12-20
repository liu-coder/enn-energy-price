package com.enn.energy.price.client.dto.response;


import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class ElectricityPriceUnifiedDetailRespDto extends ElectricityPriceValueDetailRespDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4159411044027038467L;
	
	/**
	 * 调整电费系数，允许为空
	 */
	@ApiModelProperty("电价系数")
	private BigDecimal priceRate = BigDecimal.ZERO;
}
