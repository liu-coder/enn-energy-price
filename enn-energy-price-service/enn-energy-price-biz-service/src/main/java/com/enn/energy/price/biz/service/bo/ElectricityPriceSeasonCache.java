package com.enn.energy.price.biz.service.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 电价季节缓存对象.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/2 19:21
 * @since : 1.0
 **/
@Data
public class ElectricityPriceSeasonCache implements Serializable {

    private static final long serialVersionUID = -9163479558962080176L;
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


    private List<ElectricityPriceDetailCache> electricityPriceDetailCacheList;
}
