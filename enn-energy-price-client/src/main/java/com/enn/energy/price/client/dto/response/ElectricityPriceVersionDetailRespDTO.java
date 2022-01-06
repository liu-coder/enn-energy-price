package com.enn.energy.price.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/29 10:16
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceVersionDetailRespDTO implements Serializable {

    private String versionId;
    private String versionName;
    private String startDate;
    private String endDate;
    private String priceType;
    private String enterprise;
    private String storeCode;
    private String storeName;
    private String bindType;
    private String equipmentId;
    private String systemCode;
    private String cimName;
    private String province;
    private String provinceCode;
    private String city;
    private String cityCode;
    private String district;
    private String districtCode;
    private List<VersionRuleDTO> electricityPriceRuleDTOList;
    private VersionEquipmentDTO electricityPriceEquipmentDTO;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VersionRuleDTO implements Serializable {
        private String ruleId;
        private String strategy;
        private String transformerCapacityPrice;
        private String maxCapacityPrice;
        private String industry;
        private String voltageLevel;
        private List<VersionSeasonPriceDetail> electricityPriceSeasonDTOList;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VersionSeasonPriceDetail implements Serializable {
        private String seasonId;
        private String season;
        private String seaStartDate;
        private String seaEndDate;
        private String pricingMethod;
        private List<VersionPriceDetail> electricityPriceDetailDTOList;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VersionPriceDetail implements Serializable{
        private String detailId;
        private String periods;
        private String price;
        private String endTime;
        private String startTime;
        private String step;
        private String startStep;
        private String endStep;
    }
    @Data
    public static class VersionEquipmentDTO implements Serializable {
        private String cimSystem;
        private String equipmentId;
        private String equipmentName;
        private String tenant;
    }
}
