package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 16:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureRuleDetailBO implements Serializable {
    private static final long serialVersionUID = 1055901958892154460L;

    private String id;
    private String industries;
    private String strategies;
    private String voltageLevels;

    private List<ElectricityPriceSeasonDetailBO> electricityPriceSeasonDetailBOS;


}
