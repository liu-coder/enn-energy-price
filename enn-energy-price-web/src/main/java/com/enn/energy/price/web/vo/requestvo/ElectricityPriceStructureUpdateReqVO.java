package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 10:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("电价价格体系修改vo")
public class ElectricityPriceStructureUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 804237214764593129L;
    @ApiModelProperty(value = "体系id,新增的体系不传id,修改的体系需要传")
    private Integer id;
    @ApiModelProperty(value = "体系名称",required = true)
    @NotBlank(message = "体系名称不能为空")
    private String structureName;

    @ApiModelProperty(value = "省编码",required = true)
    private String provinceCode;

    @ApiModelProperty(value = "市编码,多个用,进行拼接",required = true)
    private String cityCodes;

    @ApiModelProperty(value = "区县编码不能为空,多个用,拼接",required = true)
    private String districtCodes;

    @ApiModelProperty(value = "变更类型,2:修改")
    @NotNull(message = "变更类型不能为空")
    private Integer changeType;

    @ApiModelProperty(value = "电价价格列表",required = true)
    @NotEmpty(message = "电价价格列表不能为空")
    @Valid
    private List<ElectricityPriceUpdateReqVO> electricityPriceUpdateReqVOList;

    @ApiModelProperty(value = "电价季节分时列表",required = true)
    @NotEmpty(message = "电价季节分时列表不能为空")
    @Valid
    private List<ElectricityPriceSeasonRuleUpdateReqVO> electricityPriceSeasonRuleUpdateReqVOList;
}
