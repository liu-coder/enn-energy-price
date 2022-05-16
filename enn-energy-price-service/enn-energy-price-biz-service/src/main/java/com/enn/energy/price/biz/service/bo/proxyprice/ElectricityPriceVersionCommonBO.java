package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceVersionCommonBO implements Serializable {
    private static final long serialVersionUID = -6507351604437158608L;

    private String id;
    private String versionName;
    private String provinceCode;
    private String tenantId;
    private String tenantName;
    private String versionId;
    private String province;
    private String systemCode;
    private String systemName;
    private String priceType;
    private String bindType;
    private String lastVersionId;
    private String startDate;
    private String endDate;
    private List<ElectricityPriceStructureDetailBO> electricityPriceStructureUpdateBOList;
}
