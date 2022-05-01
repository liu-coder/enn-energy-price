package com.enn.energy.price.biz.service.bo.proxyprice;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/1
 **/
public class ElectricityPriceStructureAndRuleAndSeasonCreateBO implements Serializable {

    private static final long serialVersionUID = -2008982505149517606L;

    @Valid
    private ElectricityPriceStructureCreateBO priceStructureCreateBO;

    @Valid
    private ValidationList<ElectricityPriceStructureRuleCreateBO> priceStructureRuleCreateBOList;

    @Valid
    private ValidationList<ElectricityPriceRuleCreateBO> priceRuleCreateBOList;

    public ElectricityPriceStructureCreateBO getPriceStructureCreateBO() {
        return priceStructureCreateBO;
    }

    public void setPriceStructureCreateBO(ElectricityPriceStructureCreateBO priceStructureCreateBO) {
        this.priceStructureCreateBO = priceStructureCreateBO;
    }

    public ValidationList<ElectricityPriceStructureRuleCreateBO> getPriceStructureRuleCreateBOList() {
        return priceStructureRuleCreateBOList;
    }

    public void setPriceStructureRuleCreateBOList(ValidationList<ElectricityPriceStructureRuleCreateBO> priceStructureRuleCreateBOList) {
        this.priceStructureRuleCreateBOList = priceStructureRuleCreateBOList;
    }

    public ValidationList<ElectricityPriceRuleCreateBO> getPriceRuleCreateBOList() {
        return priceRuleCreateBOList;
    }

    public void setPriceRuleCreateBOList(ValidationList<ElectricityPriceRuleCreateBO> priceRuleCreateBOList) {
        this.priceRuleCreateBOList = priceRuleCreateBOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureAndRuleAndSeasonCreateBO{" +
                "priceStructureCreateBO=" + priceStructureCreateBO +
                ", priceStructureRuleCreateBOList=" + priceStructureRuleCreateBOList +
                ", priceRuleCreateBOList=" + priceRuleCreateBOList +
                '}';
    }
}