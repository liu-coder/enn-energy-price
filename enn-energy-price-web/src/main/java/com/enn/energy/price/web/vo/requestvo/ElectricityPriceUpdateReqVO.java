package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 11:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("电价价格请求修改vo")
public class ElectricityPriceUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 758279791811577276L;
    @ApiModelProperty(value = "用电行业",required = true)
    @NotBlank(message = "用电行业不能为空")
    private String industry;
    @ApiModelProperty(value = "定价类型 0:单一制;1:双部制",required = true)
    @NotBlank(message = "定价类型不能为空")
    private String strategy;
    @ApiModelProperty(value = "电压等级id",required = true)
    @NotBlank(message = "电压等级id不能为空")
    private String voltageLevel;
    @ApiModelProperty(value = "电度输配价格")
    @DecimalMin(value = "0",message = "电度输配价格数值必须大于0")
    private BigDecimal distributionPrice;
    @ApiModelProperty(value = "政府性附加")
    @DecimalMin( value = "0",message = "政府性附加数值必须大于0")
    private BigDecimal govAddPrice;
    @ApiModelProperty(value = "尖时价格")
    @DecimalMin( value = "0",message = "尖时价格数值必须大于0")
    private BigDecimal sharpPrice;
    @ApiModelProperty(value = "峰时价格")
    @DecimalMin( value = "0",message = "峰时价格数值必须大于0")
    private BigDecimal peakPrice;
    @ApiModelProperty(value = "平时价格")
    @DecimalMin( value = "0",message = "平时价格数值必须大于0" )
    private BigDecimal levelPrice;
    @ApiModelProperty(value = "谷时价格")
    @DecimalMin( value = "0",message = "谷时价格数值必须大于0" )
    private BigDecimal valleyPrice;
    @ApiModelProperty(value = "最大需量")
    @DecimalMin( value = "0",message = "最大需量数值必须大于0")
    private BigDecimal maxCapacityPrice;
    @ApiModelProperty(value = "变压器容量价格")
    @DecimalMin( value = "0",message = "变压器容量价格数值必须大于0")
    private BigDecimal transformerCapacityPrice;
    @ApiModelProperty(value = "价格id")
    private Long priceId;
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;
}
