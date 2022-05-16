package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityTimeSectionUpdateBO;
import com.enn.energy.price.common.enums.CompareEum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ElectricityPriceStrategyRespVO implements Serializable {
    private String compare;
    private String compareName;
    private String temperature;
    private List<ElectricityTimeSectionRespVO> electricityTimeSectionUpdateBOList;

    public String getCompareName() {
        return this.compareName = CompareEum.getDesc(this.compare);
    }

}
