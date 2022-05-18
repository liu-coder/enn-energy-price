package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 电价绑定详情响应对象
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "编辑时电价绑定详情响应对象")
public class ElectricityPriceBindEditDetailRespVO implements Serializable {

    private String nodeId;

    private String provinceCode;

    private String cityCode;

    private String areaCode;

    private ElectricityPriceBindEditDetailItemRespVO electricityPriceBindEditDetailItemRespVO;

    private String nextChangeFlag;

    private ElectricityPriceBindEditDetailItemRespVO nextVersionPriceBindVO;

}
