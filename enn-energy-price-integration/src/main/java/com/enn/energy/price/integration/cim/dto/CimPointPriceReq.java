package com.enn.energy.price.integration.cim.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：chenchangtong
 * @date ：Created 2022/1/5 14:23
 * @description：快乐工作每一天
 */
@Data
public class CimPointPriceReq {
    @NotBlank(message = "查询日期不能为空")
    private String date;
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;
    @NotBlank(message = "单位")
    private String metric;
    @NotBlank(message = "系统编码不能为空")
    private String systemCode;
}
