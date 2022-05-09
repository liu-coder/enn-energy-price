package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceSeasonDetailBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureRuleDetailRespVO implements Serializable {
    private String id;
    private String industries;
    private String strategies;
    private String voltageLevels;

    private List<ElectricityPriceSeasonDetailRespVO> priceSeasonDetailRespVOS;
}
