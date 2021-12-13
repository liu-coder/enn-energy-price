package com.enn.energy.price.client.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/12/7 11:16
 * @description：快乐工作每一天
 */
@Data
public class ElectricityPriceVersionDelDTO {
    @NotBlank(message = "版本ID不能为空")
    private String versionId;
    @NotBlank(message = "设备ID不能为空")
    private String equipmentId;
    @NotBlank(message = "systemCode不能为空")
    private String systemCode;
}
