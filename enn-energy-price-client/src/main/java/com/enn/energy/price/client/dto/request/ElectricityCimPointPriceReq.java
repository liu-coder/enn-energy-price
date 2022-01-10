package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：chenchangtong
 * @date ：Created 2022/1/5 14:23
 * @description：快乐工作每一天
 */
@Data
public class ElectricityCimPointPriceReq {
    @ApiModelProperty(required = true,name = "查询日期")
    @NotBlank(message = "查询日期不能为空")
    private String date;
    @ApiModelProperty(required = true,name = "设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;
    @ApiModelProperty(required = true,name = "单位")
    @NotBlank(message = "单位不能为空")
    private String metric;
    @ApiModelProperty(required = true,name = "编码")
    @NotBlank(message = "系统编码不能为空")
    private String systemCode;
}
