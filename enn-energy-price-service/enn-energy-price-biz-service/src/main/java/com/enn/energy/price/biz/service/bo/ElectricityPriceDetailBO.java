package com.enn.energy.price.biz.service.bo;


import lombok.Data;

import java.io.Serializable;

/**
 * 电价规则明细DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 16:29
 * @since : 1.0
 **/
@Data
public class ElectricityPriceDetailBO implements Serializable {


    private static final long serialVersionUID = -2930853052008920772L;

    /**
     * 电价版本id
     */
    private String versionId;

    /**
     * 季节id
     */
    private String seasonId;

    /**
     * 电价规则ID
     */

    private String ruleId;
    /**
     * 电价明细id
     */
    private String detailId;

    /**
     * 时段，0:尖;1:峰;2:平;3:谷
     */
    private String periods;

    /**
     * 时段开始时间
     */
    private String startTime;

    /**
     * 时段结束时间
     */
    private String endTime;

    /**
     * 阶梯定义
     */
    private String step;

    /**
     * 阶梯起码
     */
    private String startStep;

    /**
     * 阶梯止码
     */
    private String endStep;

    /**
     * 电价
     */
    private String price;

    /**
     * 删除状态
     */
    private Integer state;
}
