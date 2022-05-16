package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 14:16
 */
@ApiModel("电价季节规则请求vo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonRuleUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 6708324781222848894L;
    @ApiModelProperty(value = "体系规则id,新增的不传id,原有的传id")
    private String structureRuleId;
    @ApiModelProperty(value = "用电行业,多个用电行业用,进行分割",required = true)
    @NotBlank(message = "用电行业不能为空")
    private String industries;
    @ApiModelProperty(value = "定价类型,多个定价类型用,进行分割",required = true)
    @NotBlank(message = "定价类型不能为空")
    private String strategies;
    @ApiModelProperty(value = "电压等级,多个电压等级用,进行分割",required = true)
    @NotBlank(message = "电压等级不能为空")
    private String voltageLevels;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;
    @ApiModelProperty(value = "季节分时列表")
    @NotEmpty(message = "季节分时列表不能为空")
    @Valid
    private ValidationList<ElectricityPriceSeasonUpdateReqVO> electricityPriceSeasonUpdateReqVOList;

}
