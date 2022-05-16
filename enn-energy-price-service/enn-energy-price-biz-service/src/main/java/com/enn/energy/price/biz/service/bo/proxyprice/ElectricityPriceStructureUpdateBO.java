package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureUpdateBO implements Serializable {
    private static final long serialVersionUID = 5420388115986723646L;
    private Long id;
    private String structureId;
    private String parentid;
    private String versionId;
    private String structureName;
    private String provinceCode;
    private String cityCodes;
    private String districtCodes;
    private Integer changeType;
    private Byte state;
    private List<ElectricityPriceUpdateBO> electricityPriceUpdateBOList;
    private List<ElectricityPriceStructureRuleUpdateBO> electricityPriceStructureRuleUpdateBOList;
}
