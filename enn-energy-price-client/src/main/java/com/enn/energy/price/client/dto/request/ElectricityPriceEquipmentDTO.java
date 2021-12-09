package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 设备DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 16:22
 * @since : 1.0
 **/
@Data
public class ElectricityPriceEquipmentDTO implements Serializable {

    private static final long serialVersionUID = -753644610066817975L;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", required = true)
    @NotBlank(message = "设备id必填")
    private String equipmentId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", required = true)
    @NotBlank(message = "设备名称必填")
    private String equipmentName;

    /**
     * 企业租户
     */
    private String tenant;

//    /**
//     * 系统编码
//     */
//    @ApiModelProperty(value = "系统编码")
//    @NotBlank(message = "系统编码必填")
//    private String systemCode;
}
