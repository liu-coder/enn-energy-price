package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceVersionBO implements Serializable {
    private static final long serialVersionUID = 6565281693056126369L;

    private Long id;

    private String versionId;

    private String versionName;

    private String provinceCode;

    private String province;

    private String cityCode;

    private String city;

    private String districtCode;

    private String district;

    private String tenantId;

    private String tenantName;

    private String systemCode;

    private String systemName;

    private String equipmentId;

    private String equipmentName;

    private Date startDate;

    private Date endDate;

    private String priceType;

    private String bindType;

    private Integer state;

    private String creator;

    private String updator;

    private Date createTime;

    private Date updateTime;

    private String lastVersionId;
}
