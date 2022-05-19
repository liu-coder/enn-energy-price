package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/6 17:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDateBO implements Serializable {
    private static final long serialVersionUID = 5345018996539374173L;
    private String seasonId;
    private String seaStartDate;
    private String seaEndDate;
    private Integer changeType;
}
