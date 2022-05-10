package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:52
 */
public class ElectricityPriceStructureRuleDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -986937200518447087L;

    private String industries;

    private String strategies;

    private String voltageLevels;

    private List<ElectricityPriceSeasonDetailForCreateRespVO> priceSeasonDetailForCreateRespVOList;

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }

    public String getStrategies() {
        return strategies;
    }

    public void setStrategies(String strategies) {
        this.strategies = strategies;
    }

    public String getVoltageLevels() {
        return voltageLevels;
    }

    public void setVoltageLevels(String voltageLevels) {
        this.voltageLevels = voltageLevels;
    }

    public List<ElectricityPriceSeasonDetailForCreateRespVO> getPriceSeasonDetailForCreateRespVOList() {
        return priceSeasonDetailForCreateRespVOList;
    }

    public void setPriceSeasonDetailForCreateRespVOList(List<ElectricityPriceSeasonDetailForCreateRespVO> priceSeasonDetailForCreateRespVOList) {
        this.priceSeasonDetailForCreateRespVOList = priceSeasonDetailForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureRuleDetailForCreateRespVO{" +
                "industries='" + industries + '\'' +
                ", strategies='" + strategies + '\'' +
                ", voltageLevels='" + voltageLevels + '\'' +
                ", priceSeasonDetailForCreateRespVOList=" + priceSeasonDetailForCreateRespVOList +
                '}';
    }
}
