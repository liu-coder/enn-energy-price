package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectricityPriceVersionsByBindAreaBO implements Serializable {

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private List<ElectricityPriceVersionStructureBO> electricityPriceVersionBOS;
}
