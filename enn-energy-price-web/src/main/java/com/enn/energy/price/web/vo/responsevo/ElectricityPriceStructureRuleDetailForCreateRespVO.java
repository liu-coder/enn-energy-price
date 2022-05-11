package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("季节分时对应的体系详情响应VO")
public class ElectricityPriceStructureRuleDetailForCreateRespVO implements Serializable {

    private static final long serialVersionUID = -986937200518447087L;

    @ApiModelProperty(value = "用电行业", dataType = "string")
    private String industries;

    @ApiModelProperty(value = "定价策略", dataType = "string")
    private String strategies;

    @ApiModelProperty(value = "电压等级", dataType = "string")
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
