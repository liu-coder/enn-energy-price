package com.enn.energy.price.biz.service.bo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 电价规则DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 16:06
 * @since : 1.0
 **/
@Data
public class ElectricityPriceRuleBO implements Serializable {


    private static final long serialVersionUID = 8876416217208769247L;

    /**
     * 电价规则id
     */
    private String ruleId;

    /**
     * 电价版本id
     */
    private String versionId;

    /**
     * 用电行业
     */
    private String industry;

    /**
     * 定价策略，1:单一制;2:两部制
     */
    private String strategy;

    /**
     * 电压等级id
     */
    private String voltageLevel;

    /**
     * 变压器容量基础电价
     */
    private String transformerCapacityPrice;

    /**
     * 最大容量基础电价
     */
    private String maxCapacityPrice;

    /**
     * 电价规则对应的季节
     */
    private List<ElectricityPriceSeasonBO> electricityPriceSeasonBOList;

    /**
     * 企业租户id
     */
    private String tenantId;

    /**
     * 企业租户名称
     */
    private String tenantName;

}
