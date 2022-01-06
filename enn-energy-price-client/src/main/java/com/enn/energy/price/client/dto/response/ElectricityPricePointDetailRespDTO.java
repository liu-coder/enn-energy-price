package com.enn.energy.price.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2022/1/5 14:50
 * @description：快乐工作每一天
 */
@Data
public class ElectricityPricePointDetailRespDTO implements Serializable {
    private String date;
    private String deviceId;
    private String metric;
    private String systemCode;
    private List<ElectricityCimPointPriceDetail> priceDataList;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ElectricityCimPointPriceDetail{
        private BigDecimal ladderEndValue ;
        private String ladderName ;
        private BigDecimal ladderStartValue ;
        private BigDecimal price ;
        private String timeShareEndDate ;
        private String timeShareLevel ;
        private String timeShareStartDate ;
        private String timeShareType;
    }
}
