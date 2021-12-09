package com.enn.energy.price.client.dto.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一电价查询dto接口
 * @author wenjianping
 *
 */
@Data
public class EletricityUnifiedReqDto extends ElectricityPriceValueReqDTO{

	/**
	 * 电价类型
	 */
	@ApiModelProperty("电价类型")
	private String type;

}
