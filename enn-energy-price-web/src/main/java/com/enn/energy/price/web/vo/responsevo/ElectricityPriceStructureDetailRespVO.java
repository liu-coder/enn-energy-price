package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceDetailBO;
import com.enn.energy.price.biz.service.bo.proxyprice.ElectricityPriceStructureRuleDetailBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityPriceStructureDetailRespVO implements Serializable {
    private String id;
    private String structureName;
    private String provinceCode;
    private String cityCodes;
    private String districtCodes;
    private List<ElectricityPriceDetailRespVO> priceDetailRespVOList;
    private List<ElectricityPriceStructureRuleDetailRespVO> structureRuleDetailRespVOList;
}
