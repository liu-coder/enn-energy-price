package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 14:53
 */
@ApiModel("电价季节分时请求vo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonUpdateRespVO implements Serializable {
    private static final long serialVersionUID = 6708324781222848894L;
    @ApiModelProperty(value = "季节id,新增的不传,修改的需要")
    private String seasonSectionId;
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;
    @ApiModelProperty("季节时间列表")
    @NotEmpty(message = "季节时间列表不能为空")
    @Valid
    private List<SeasonDateRespVO> seasonSectionRespVOList;

    @ApiModelProperty(value = "修正策略列表")
    @Valid
    private List<ElectricityPriceStrategyRespVO> structureRuleSeasonStrategyRespVOList;

}
