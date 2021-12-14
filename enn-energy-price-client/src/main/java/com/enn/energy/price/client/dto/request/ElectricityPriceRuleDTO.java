package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 电价规则DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 16:06
 * @since : 1.0
 **/
@Data
public class ElectricityPriceRuleDTO implements Serializable {

    private static final long serialVersionUID = -6683048311124634104L;
    /**
     * 电价规则id
     */
//    @ApiModelProperty(value = "电价规则id", required = true)
//    @NotNull(message = "电价规则id必填")
 //   private String ruleId;

//    /**
//     * 电价版本id
//     */
//    private String versionId;

    /**
     * 用电行业
     */
    private String industry;

    /**
     * 定价策略，0:单一制;1:双部制
     */
    @ApiModelProperty(value = "定价策略", example = "0:单一制;1:双部制", required = true)
    @NotBlank(message = "定价策略必填")
    private String strategy;

    /**
     * 电压等级id
     */
    private String voltageLevel;

    /**
     * 变压器容量基础电价
     */
    private String transformerCapacityPrice;

    /**
     * 最大容量基础电价
     */
    private String maxCapacityPrice;

    /**
     * 电价规则对应的季节
     */
    @ApiModelProperty(value = "规则对应的季节", required = true)
    @NotNull(message = "规则对应的季节不能为空")
    @NotEmpty(message = "规则对应的季节不能为空")
    @Valid
    private List<ElectricityPriceSeasonDTO> electricityPriceSeasonDTOList;

}
