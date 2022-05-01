package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新建版本总BO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
public class ElectricityPriceVersionStructuresCreateBO implements Serializable {

    private static final long serialVersionUID = -9169380011531279714L;

    private ElectricityPriceVersionCreateBO priceVersionCreateBO;

    private ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateBO> priceStructureAndRuleAndSeasonCreateBOList;

    public ElectricityPriceVersionCreateBO getPriceVersionCreateBO() {
        return priceVersionCreateBO;
    }

    public void setPriceVersionCreateBO(ElectricityPriceVersionCreateBO priceVersionCreateBO) {
        this.priceVersionCreateBO = priceVersionCreateBO;
    }

    public ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateBO> getPriceStructureAndRuleAndSeasonCreateBOList() {
        return priceStructureAndRuleAndSeasonCreateBOList;
    }

    public void setPriceStructureAndRuleAndSeasonCreateBOList(ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateBO> priceStructureAndRuleAndSeasonCreateBOList) {
        this.priceStructureAndRuleAndSeasonCreateBOList = priceStructureAndRuleAndSeasonCreateBOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceVersionStructuresCreateBO{" +
                "priceVersionCreateBO=" + priceVersionCreateBO +
                ", priceStructureAndRuleAndSeasonCreateBOList=" + priceStructureAndRuleAndSeasonCreateBOList +
                '}';
    }
}