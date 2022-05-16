package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 根据区域获取有效版本列表响应对象
 * @author:quyl
 * @createTime:2022/5/4 22:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("电价版本响应list")
@Builder
public class ElectricityPriceVersionsByBindAreaRespVO implements Serializable {
    @ApiModelProperty(name = "省code")
    private String provinceCode;

    @ApiModelProperty(name = "市code")
    private String cityCode;

    @ApiModelProperty(name = "区code")
    private String districtCode;

    List<ElectricityPriceVersionRespVO> electricityPriceVersionRespVOList;
}
