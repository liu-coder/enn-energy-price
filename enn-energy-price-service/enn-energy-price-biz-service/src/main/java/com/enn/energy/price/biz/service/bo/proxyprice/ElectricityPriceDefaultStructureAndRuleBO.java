package com.enn.energy.price.biz.service.bo.proxyprice;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:36
 */

public class ElectricityPriceDefaultStructureAndRuleBO implements Serializable {

    private static final long serialVersionUID = 3052881996864261585L;

    private ElectricityPriceStructureRuleDetailBO priceStructureRuleDetailBO;

    private List<ElectricityPriceDetailBO> priceDetailBOList;

    public ElectricityPriceStructureRuleDetailBO getPriceStructureRuleDetailBO() {
        return priceStructureRuleDetailBO;
    }

    public void setPriceStructureRuleDetailBO(ElectricityPriceStructureRuleDetailBO priceStructureRuleDetailBO) {
        this.priceStructureRuleDetailBO = priceStructureRuleDetailBO;
    }

    public List<ElectricityPriceDetailBO> getPriceDetailBOList() {
        return priceDetailBOList;
    }

    public void setPriceDetailBOList(List<ElectricityPriceDetailBO> priceDetailBOList) {
        this.priceDetailBOList = priceDetailBOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceDefaultStructureAndRuleBO{" +
                "priceStructureRuleDetailBO=" + priceStructureRuleDetailBO +
                ", priceDetailBOList=" + priceDetailBOList +
                '}';
    }
}
