package com.enn.energy.price.client.dto.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一电价查询dto接口
 * @author wenjianping
 *
 */
@Data
@ApiModel(description = "统一电价查询入参")
public class EletricityUnifiedReqDto implements Serializable {

	/**
	 * 电价类型
	 */
	@ApiModelProperty(value="电价类型, 1(自定义电价),2(目录电价),3单一电价(自定义电价)", required=true)
	@NotNull(message = "priceType不能为空")
	@Min(value = 1, message = "电价类型为1-3的数字")
	@Max(value = 3, message = "电价类型为1-3的数字")
	private Integer priceType;
	
    @ApiModelProperty(value="租户id。在V1.1过度版本中传systemCode", required=true)
	@NotEmpty(message = "租户id不能为空")
	private String tenantId;
	
    @ApiModelProperty(value="设备id。在V1.1过度版本中传deviceCode ", required=true)
	@NotEmpty(message = "有效时间不能为空")
	private String deviceNumber;
	
    @ApiModelProperty(value="有效时间 yyyy-MM-dd", required=true)
	@NotEmpty(message = "有效时间不能为空")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "有效时间格式为yyyy-MM-dd")
	private String effectiveTime;

}
