package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.web.validator.DecimalValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 电价规则请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("电价规则请求VO")
public class ElectricityPriceRuleValidateReqVO implements Serializable {

    private static final long serialVersionUID = -583011369969060471L;

    @ApiModelProperty(value = "主键id", required = false, dataType = "string")
    private Long id;

    @ApiModelProperty(value = "规则id", required = false, dataType = "string")
    private String ruleId;

    @ApiModelProperty(value = "当前规则所在的行号", required = true, dataType = "string")
    private String serialNo;

    @ApiModelProperty(value = "用电分类", required = true, dataType = "string")
    @NotBlank(message = "用电分类不能为空")
    private String industry;

    @ApiModelProperty(value = "定价类型 0:单一制;1:双部制", required = true, dataType = "string")
    @NotBlank(message = "定价类型不能为空")
    private String strategy;

    @ApiModelProperty(value = "电压等级", required = true, dataType = "string")
    @NotBlank(message = "电压等级不能为空")
    private String voltageLevel;

    @ApiModelProperty(value = "变压器容量", required = false, dataType = "string")
    @DecimalValue(message = "必须填写正数")
    private String transformerCapacityPrice;

    @ApiModelProperty(value = "最大需量", required = false, dataType = "string")
    @DecimalValue(message = "必须填写正数")
    private String maxCapacityPrice;

    @NotNull(message = "电价不能为空")
    @Valid
    private ElectricityPriceValidateReqVO electricityPriceValidateVO;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public String getTransformerCapacityPrice() {
        return transformerCapacityPrice;
    }

    public void setTransformerCapacityPrice(String transformerCapacityPrice) {
        this.transformerCapacityPrice = transformerCapacityPrice;
    }

    public String getMaxCapacityPrice() {
        return maxCapacityPrice;
    }

    public void setMaxCapacityPrice(String maxCapacityPrice) {
        this.maxCapacityPrice = maxCapacityPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public ElectricityPriceValidateReqVO getElectricityPriceValidateVO() {
        return electricityPriceValidateVO;
    }

    public void setElectricityPriceValidateVO(ElectricityPriceValidateReqVO electricityPriceValidateVO) {
        this.electricityPriceValidateVO = electricityPriceValidateVO;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public String toString() {
        return "ElectricityPriceRuleValidateReqVO{" +
                "id=" + id +
                ", ruleId='" + ruleId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", industry='" + industry + '\'' +
                ", strategy='" + strategy + '\'' +
                ", voltageLevel='" + voltageLevel + '\'' +
                ", transformerCapacityPrice='" + transformerCapacityPrice + '\'' +
                ", maxCapacityPrice='" + maxCapacityPrice + '\'' +
                ", electricityPriceValidateVO=" + electricityPriceValidateVO +
                '}';
    }
}