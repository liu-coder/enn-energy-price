package com.enn.energy.price.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/24 16:09
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceVersionsRespDTO implements Serializable {
    private String versionId;
//    private String cimCode;
    private String versionName;
    private String seasons;
    private String startDate;
    private String endTime;
    private Integer versionStatus;
}
