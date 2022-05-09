package com.enn.energy.price.web.vo.responsevo;

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
    private Integer timeSectionId;
    private String periods;
    private String startTime;
    private String endTime;
}
