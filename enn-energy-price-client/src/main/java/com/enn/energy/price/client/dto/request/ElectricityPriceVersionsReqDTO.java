package com.enn.energy.price.client.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/24 16:08
 * @description：快乐工作每一天
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ElectricityPriceVersionsReqDTO extends BasePageDto {
    @NotBlank(message = "设备ID不能为空")
    private String equipmentId;
    @NotBlank(message = "cim系统code不能为空")
    private String systemCode;

}
