package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.common.enums.PeriodsEum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityTimeSectionRespVO implements Serializable {
    private Long id;
    private String periods;
    private String periodsName;
    private String startTime;
    private String endTime;

    public String getPeriodsName() {
        return PeriodsEum.getDesc( periods );
    }
}
