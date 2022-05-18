package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 电价绑定详情响应对象
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ElectricityPriceBindEditDetailBO implements Serializable {

    private String nodeId;

    private String provinceCode;

    private String cityCode;

    private String areaCode;

    private ElectricityPriceBindEditDetailItemBO electricityPriceBindEditDetailItemBO;

    private String nextChangeFlag;

    private ElectricityPriceBindEditDetailItemBO nextVersionPriceBindDetailItemBO;

}
