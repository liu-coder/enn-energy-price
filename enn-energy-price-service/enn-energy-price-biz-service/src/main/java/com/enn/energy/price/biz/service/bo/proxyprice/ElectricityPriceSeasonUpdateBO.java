package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 17:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonUpdateBO implements Serializable {
    private static final long serialVersionUID = 3323637832408269048L;
    private String seasonSectionId;
    private String seasonName;
    private Integer changeType;
    private ValidationList<SeasonDateBO> seasonDateList;
    private ValidationList<ElectricityPriceStrategyBO> electricityPriceStrategyBOList;

}
