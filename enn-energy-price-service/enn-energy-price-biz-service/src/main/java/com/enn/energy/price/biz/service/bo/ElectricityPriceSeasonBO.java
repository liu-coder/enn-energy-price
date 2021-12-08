package com.enn.energy.price.biz.service.bo;

import lombok.Data;

import java.util.List;

/**
 * 电价季节BO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/1 13:50
 * @since : 1.0
 **/
@Data
public class ElectricityPriceSeasonBO {

    /**
     * 季节id
     */
    private String seasonId;

    /**
     * 电价规则id
     */
    private String ruleId;

    /**
     * 电价版本id
     */
    private String versionId;

    /**
     * 定价方式
     */
    private String pricingMethod;

    /**
     * 季节开始时间
     */
    private String seaStartDate;

    /**
     * 季节结束时间
     */
    private String seaEndDate;

    /**
     * 季节
     */
    private String season;

    /**
     * 电价规则明细
     */
    private List<ElectricityPriceDetailBO> electricityPriceDetailBOList;

}
