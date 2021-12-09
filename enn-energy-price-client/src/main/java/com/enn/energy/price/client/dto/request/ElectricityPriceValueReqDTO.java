package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/22 10:31
 * @description：快乐工作每一天
 */
@Data
@ApiModel(description = "查询电价")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectricityPriceValueReqDTO implements Serializable {

    @ApiModelProperty("租户ID")
    @NotBlank(message = "cimCode不能为空")
    private String systemCode;
    @ApiModelProperty("设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String equipmentId;
    @ApiModelProperty("有效时间 yyyy-MM-dd")
    @NotBlank(message = "有效时间不能为空")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String effectiveTime;
}
