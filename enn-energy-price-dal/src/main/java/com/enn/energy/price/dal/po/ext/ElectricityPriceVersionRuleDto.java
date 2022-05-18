package com.enn.energy.price.dal.po.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 设备版本体系价格绑定关系
 * @author:quyl
 * @createTime:2022/5/18 6:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceVersionRuleDto {

    private String versionId;
    
    private String provinceCode;

    private String structureId;

    private String cityCodes;

    private String districtCodes;

    private String ruleId;

    private String industry;

    private String strategy;

    private String voltageLevel;

    private String structureRuleId;


}
