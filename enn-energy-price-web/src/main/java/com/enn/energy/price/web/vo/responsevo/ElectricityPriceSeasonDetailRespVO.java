package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonDetailRespVO implements Serializable {
    private Long seasonSectionId;
    private String seasonName;
    private List<SeansonDateRespVO> seansonDateRespVOS;
    private List<ElectricityPriceStrategyRespVO> electricityPriceStrategyVOS;
}
