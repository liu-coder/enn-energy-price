package com.enn.energy.price.biz.service.bo.proxyprice;

import com.enn.energy.price.dal.po.mbg.ElectricityPriceStructureRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 16:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureDetailBO implements Serializable {
    private static final long serialVersionUID = 2338373541655608839L;
    private String id;
    private String structureName;
    private String provinceCode;
    private String cityCodes;
    private String districtCodes;
    private List<ElectricityPriceDetailBO> electricityPriceDetailBOList;
    private List<ElectricityPriceStructureRuleDetailBO> electricityPriceStructureRuleDetailBOS;
}
