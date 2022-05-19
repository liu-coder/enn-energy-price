package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
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
@ApiModel("电价版本响应vo")
public class ElectricityPriceVersionStructureRespVO implements Serializable {

    private String versionId;

    private String versionName;

    private List<ElectricityPriceStructureRespVO> electricityPriceStructureRespVOList;
}
