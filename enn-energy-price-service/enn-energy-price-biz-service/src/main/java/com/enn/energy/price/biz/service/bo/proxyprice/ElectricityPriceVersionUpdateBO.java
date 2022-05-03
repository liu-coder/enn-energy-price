package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceVersionUpdateBO implements Serializable {
    private static final long serialVersionUID = -6507351604437158608L;

    private Long id;
    private String versionName;
    private String province;
    private String tenantId;
    private String tenantName;
    private String startDate;
    private String endDate;
    private List<ElectricityPriceStructureUpdateBO> electricityPriceStructureUpdateBOList;
}