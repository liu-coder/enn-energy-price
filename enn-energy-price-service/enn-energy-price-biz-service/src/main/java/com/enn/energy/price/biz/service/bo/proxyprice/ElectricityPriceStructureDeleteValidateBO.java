package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/12 10:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureDeleteValidateBO implements Serializable {
    private String structureId;
    private String versionId;
    private String startDate;
    private String endDate;
    private String provinceCode;
    private String cityCodes;
    private String districtCodes;
}
