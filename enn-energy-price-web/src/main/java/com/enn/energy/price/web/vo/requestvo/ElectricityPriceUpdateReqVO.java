package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.web.validator.DecimalValue;
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
    @ApiModelProperty(value = "用电分类",required = true)
    @NotBlank(message = "用电分类不能为空")
    private String industry;
    @ApiModelProperty(value = "定价类型 0:单一制;1:双部制",required = true)
    @NotBlank(message = "定价类型不能为空")
    private String strategy;
    @ApiModelProperty(value = "电压等级id",required = true)
    @NotBlank(message = "电压等级id不能为空")
    private String voltageLevel;
    @ApiModelProperty(value = "变压器容量", required = false, dataType = "string")
    @DecimalValue(message = "变压器容量价格数值校验不合法")
    private String transformerCapacityPrice;
    @ApiModelProperty(value = "最大需量", required = false, dataType = "string")
    @DecimalValue(message = "最大需量数值校验不合法")
    private String maxCapacityPrice;
    @ApiModelProperty(value = "电度用电价格", required = false, dataType = "string")
    @DecimalValue(message = "电度用电价格数值校验不合法")
    private String consumptionPrice;
    @ApiModelProperty(value = "电度输配价格", required = false, dataType = "string")
    @DecimalValue(message = "电度输配价格数值校验不合法")
    private String distributionPrice;
    @ApiModelProperty(value = "政府附加价格", required = false, dataType = "string")
    @DecimalValue(message = "政府附加价格数值校验不合法")
    private String govAddPrice;
    @ApiModelProperty(value = "尖价", required = false, dataType = "string")
    @DecimalValue(message = "尖时段价格数值校验不合法")
    private String sharpPrice;
    @ApiModelProperty(value = "峰价", required = false, dataType = "string")
    @DecimalValue(message = "峰时段价格数值校验不合法")
    private String peakPrice;
    @ApiModelProperty(value = "平价", required = false, dataType = "string")
    @DecimalValue(message = "平时段价格数值校验不合法")
    private String levelPrice;
    @ApiModelProperty(value = "谷价", required = false, dataType = "string")
    @DecimalValue(message = "谷时段价格数值校验不合法")
    private String valleyPrice;
    @ApiModelProperty(value = "价格主键id")
    private Long priceId;
    @ApiModelProperty(value = "规则主键id")
    private Long ruleId;
    @ApiModelProperty(value = "变更类型")
    private Integer ruleChangeType;
    @ApiModelProperty(value = "行号")
    private String serialNo;
}
