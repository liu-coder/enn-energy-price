package com.enn.energy.price.client.dto.request;


import javax.validation.constraints.NotBlank;

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
	@ApiModelProperty(value="电价类型, 1(自定义电价),2(目录电价),3单一电价(自定义电价)", required=true)
	@NotBlank(message = "priceType不能为空")
	private Integer priceType;
	
    @ApiModelProperty(value="租户id。在V1.1过度版本中传systemCode", required=true)
    @NotBlank(message = "租户id不能为空")
	private String tenantId;
	
    @ApiModelProperty(value="设备id。在V1.1过度版本中传deviceCode ", required=true)
    @NotBlank(message = "有效时间不能为空")
	private String deviceNumber;
	
    @ApiModelProperty(value="有效时间 yyyy-MM-dd", required=true)
    @NotBlank(message = "有效时间不能为空")
	private String effectiveTime;

}
