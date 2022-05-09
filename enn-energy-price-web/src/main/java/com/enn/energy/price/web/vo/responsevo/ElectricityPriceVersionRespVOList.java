package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("电价版本响应list")
@Builder
public class ElectricityPriceVersionRespVOList implements Serializable {
    List<ElectricityPriceVersionRespVO> electricityPriceVersionRespVOList;
}
