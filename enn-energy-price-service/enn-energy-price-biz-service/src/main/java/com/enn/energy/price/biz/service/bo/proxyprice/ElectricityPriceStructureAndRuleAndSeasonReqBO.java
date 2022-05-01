package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新建电价体系的总请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("新建电价体系的总请求VO")
public class ElectricityPriceStructureAndRuleAndSeasonReqBO implements Serializable {

    @NotNull(message = "新增体系不能为空")
    @Valid
    private ElectricityPriceStructureReqBO priceStructureReqVO;

    @NotEmpty(message = "季节分时对应的体系不能为空")
    @Valid
    private ValidationList<ElectricityPriceStructureRuleReqBO> priceStructureRuleReqVOList;

    @NotEmpty(message = "电价规则不能为空")
    @Valid
    private ValidationList<ElectricityPriceRuleCreateBO> priceRuleReqVOList;

    public ElectricityPriceStructureReqBO getPriceStructureReqVO() {
        return priceStructureReqVO;
    }

    public void setPriceStructureReqVO(ElectricityPriceStructureReqBO priceStructureReqVO) {
        this.priceStructureReqVO = priceStructureReqVO;
    }

    public ValidationList<ElectricityPriceStructureRuleReqBO> getPriceStructureRuleReqVOList() {
        return priceStructureRuleReqVOList;
    }

    public void setPriceStructureRuleReqVOList(ValidationList<ElectricityPriceStructureRuleReqBO> priceStructureRuleReqVOList) {
        this.priceStructureRuleReqVOList = priceStructureRuleReqVOList;
    }

    public ValidationList<ElectricityPriceRuleCreateBO> getPriceRuleReqVOList() {
        return priceRuleReqVOList;
    }

    public void setPriceRuleReqVOList(ValidationList<ElectricityPriceRuleCreateBO> priceRuleReqVOList) {
        this.priceRuleReqVOList = priceRuleReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureAndRuleAndSeasonReqVO{" +
                "priceStructureReqVO=" + priceStructureReqVO +
                ", priceStructureRuleReqVOList=" + priceStructureRuleReqVOList +
                ", priceRuleReqVOList=" + priceRuleReqVOList +
                '}';
    }
}