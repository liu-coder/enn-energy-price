package com.enn.energy.price.biz.service.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 电价明细缓存对象.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/2 19:22
 * @since : 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceDetailCache implements Serializable {

    private static final long serialVersionUID = 6386224931179857006L;

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

}
