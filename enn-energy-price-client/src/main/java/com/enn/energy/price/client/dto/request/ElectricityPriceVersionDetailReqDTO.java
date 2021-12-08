package com.enn.energy.price.client.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/29 10:14
 * @description：快乐工作每一天
 */
@Data
public class ElectricityPriceVersionDetailReqDTO {
    @NotBlank(message = "设备ID不能为空")
    private String equipmentId;
    @NotBlank(message = "cim系统code不能为空")
    private String systemCode;
    @NotBlank(message = "版本ID不能为空")
    private String versionId;
}
