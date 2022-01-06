package com.enn.energy.price.biz.service.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 电价版本DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 15:53
 * @since : 1.0
 **/
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceVersionBO implements Serializable, Comparable<ElectricityPriceVersionBO> {


    private static final long serialVersionUID = 983625301693655951L;

    /**
     * 电价版本id
     */
    private String versionId;

    /**
     * 电价版本名称
     */
    private String versionName;

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 省
     */
    private String province;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 市
     */
    private String city;

    /**
     * 区、县编码
     */
    private String districtCode;

    /**
     * 区、县
     */
    private String district;

    /**
     * 企业
     */
    private String enterprise;

    /**
     * 设备ID
     */
    private String equipmentId;
    /**
     * 系统编码
     */
    private String systemCode;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 企业租户id
     */
    private String tenantId;

    /**
     * 企业租户名称
     */
    private String tenantName;

    /**
     * 生效日期
     */
    private Date startDate;

    /**
     * 失效日期
     */
    private Date endDate;

    /**
     * 电价类型,0:目录电价;1:自定义电价
     */
    private String priceType;

    /**
     * 绑定类型,1:设备
     */
    private String bindType;

    /**
     * 上一个版本id
     */
    private String lastVersionId;

    /**
     * 版本对应的规则
     */
    private List<ElectricityPriceRuleBO> electricityPriceRuleBOList;

    /**
     * 设备
     */
    private ElectricityPriceEquipmentBO electricityPriceEquipmentBO;

    /**
     * 商品编码
     */
    private String commodityCode;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 更新时间
     */
    private Date  updateTime;

    @Override
    public int compareTo(ElectricityPriceVersionBO o) {

        return this.getStartDate().compareTo(o.getStartDate());
    }
}
