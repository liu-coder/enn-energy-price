package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 电价绑定详情响应对象
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "编辑时电价绑定详情响应对象")
public class ElectricityPriceBindEditDetailItemRespVO implements Serializable {

    @ApiModelProperty(required = true, name = "设备绑定id")
    private Long id;

    private String versionId;

    private String structureId;

    private String startDate;

    private String endDate;

    private String ruleId;

    /**
     * 体系列表
     */
    List<ElectricityPriceStructureRespVO> electricityPriceStructureRespVOList;

    ElectricityPriceStructureDetailRespVO electricityPriceStructureDetailRespVO;

}
