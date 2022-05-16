package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 17:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStrategyBO implements Serializable {
    private static final long serialVersionUID = 6502612562096991373L;
    private String compare;
    private String temperature;
    private Integer changeType;
    private Boolean comply;
    private ValidationList<ElectricityTimeSectionUpdateBO> electricityTimeSectionUpdateBOList;
}
