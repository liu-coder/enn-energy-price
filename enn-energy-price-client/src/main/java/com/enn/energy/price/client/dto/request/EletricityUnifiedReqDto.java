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
public class EletricityUnifiedReqDto {

	/**
	 * 电价类型
	 */
	@ApiModelProperty("电价类型, 1(自定义电价),2(目录电价),3单一电价(自定义电价)")
	@NotBlank(message = "priceType不能为空")
	private Integer priceType;
	
    @ApiModelProperty("租户id。在V1.1过度版本中传systemCode")
    @NotBlank(message = "租户id不能为空")
	private String tenantId;
	
    @ApiModelProperty("设备id。在V1.1过度版本中传deviceCode ")
    @NotBlank(message = "有效时间不能为空")
	private String deviceNumber;
	
    @ApiModelProperty("有效时间 yyyy-MM-dd")
    @NotBlank(message = "有效时间不能为空")
	private String effectiveTime;

}
