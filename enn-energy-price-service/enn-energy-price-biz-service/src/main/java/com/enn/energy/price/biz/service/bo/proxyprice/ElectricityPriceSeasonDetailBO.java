package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonDetailBO implements Serializable {
    private static final long serialVersionUID = 3493749132121267258L;
    private String seasonSectionId;
    private String seasonName;
    private List<SeasonDateBO> seasonDateList;
    private List<ElectricityPriceStrategyBO> electricityPriceStrategyBOList;
}
