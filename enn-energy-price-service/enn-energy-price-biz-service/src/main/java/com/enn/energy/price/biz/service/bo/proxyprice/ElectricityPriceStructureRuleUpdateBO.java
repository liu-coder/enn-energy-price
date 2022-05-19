package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 17:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureRuleUpdateBO implements Serializable {
    private static final long serialVersionUID = -9126779565518348911L;
    private String structureRuleId;
    private String industries;
    private String strategies;
    private String voltageLevels;
    private Integer changeType;
    private String serialNo;
    private List<ElectricityPriceSeasonUpdateBO> electricityPriceSeasonUpdateReqVOList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricityPriceStructureRuleUpdateBO that = (ElectricityPriceStructureRuleUpdateBO) o;
        return getIndustries().equals(that.getIndustries()) && getStrategies().equals(that.getStrategies()) && getVoltageLevels().equals(that.getVoltageLevels());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndustries(), getStrategies(), getVoltageLevels());
    }
}
