package com.enn.energy.price.biz.service.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 16:22
 * @since : 1.0
 **/
@Data
public class ElectricityPriceEquipmentBO implements Serializable {


    private static final long serialVersionUID = -8119903193908670360L;

    /**
     * 设备id
     */
    private String equipmentId;

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 版本ID
     */
    private String versionId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 企业租户id
     */
    private String tenantId;

    /**
     * 企业租户名称
     */
    private String tenantName;

    /**
     * 系统编码
     */
    private String systemCode;
    private Integer state;
    private Date createTime;
    private Date updateTime;

}
