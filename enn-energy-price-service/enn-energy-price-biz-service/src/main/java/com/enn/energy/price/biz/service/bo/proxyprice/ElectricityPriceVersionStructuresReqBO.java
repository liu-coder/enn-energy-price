package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新建版本总请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("新建版本总请求VO")
public class ElectricityPriceVersionStructuresReqBO implements Serializable {

    private static final long serialVersionUID = -9169380011531279714L;

    @NotNull(message = "新增版本不能为空")
    @Valid
    private ElectricityPriceVersionReqBO priceVersionReqVO;

    @NotEmpty(message = "新增体系的内容不能为空")
    @Valid
    private ValidationList<ElectricityPriceStructureAndRuleAndSeasonReqBO> priceStructureAndRuleAndSeasonReqVOList;

    public ElectricityPriceVersionReqBO getPriceVersionReqVO() {
        return priceVersionReqVO;
    }

    public void setPriceVersionReqVO(ElectricityPriceVersionReqBO priceVersionReqVO) {
        this.priceVersionReqVO = priceVersionReqVO;
    }

    public ValidationList<ElectricityPriceStructureAndRuleAndSeasonReqBO> getPriceStructureAndRuleAndSeasonReqVOList() {
        return priceStructureAndRuleAndSeasonReqVOList;
    }

    public void setPriceStructureAndRuleAndSeasonReqVOList(ValidationList<ElectricityPriceStructureAndRuleAndSeasonReqBO> priceStructureAndRuleAndSeasonReqVOList) {
        this.priceStructureAndRuleAndSeasonReqVOList = priceStructureAndRuleAndSeasonReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceVersionStructuresAddReqVO{" +
                "priceVersionReqVO=" + priceVersionReqVO +
                ", priceStructureAndRuleAndSeasonReqVOList=" + priceStructureAndRuleAndSeasonReqVOList +
                '}';
    }
}