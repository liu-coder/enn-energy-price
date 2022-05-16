package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 季节列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonListRespVO implements Serializable {
    private Long seasonSectionId;
    private String seasonName;
    private List<SeansonDateRespVO> seansonDateRespVOS;
    private List<ElectricityPriceStrategyRespVO> electricityPriceStrategyVOS;
}
