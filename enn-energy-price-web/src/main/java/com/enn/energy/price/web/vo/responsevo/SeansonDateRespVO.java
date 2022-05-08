package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeansonDateRespVO implements Serializable {
    private String seasonId;
    private String seaStartDate;
    private String seaEndDate;
}
