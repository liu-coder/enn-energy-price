package com.enn.energy.price.web.vo.requestvo;

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
public class ElectricityPriceVersionStructuresReqVO implements Serializable {

    private static final long serialVersionUID = -9169380011531279714L;

    @NotNull(message = "新增版本不能为空")
    @Valid
    private ElectricityPriceVersionReqVO priceVersionReqVO;

    @NotEmpty(message = "新增体系的内容不能为空")
    @Valid
    private ValidationList<ElectricityPriceStructureAndRuleAndSeasonReqVO> priceStructureAndRuleAndSeasonReqVOList;

    public ElectricityPriceVersionReqVO getPriceVersionReqVO() {
        return priceVersionReqVO;
    }

    public void setPriceVersionReqVO(ElectricityPriceVersionReqVO priceVersionReqVO) {
        this.priceVersionReqVO = priceVersionReqVO;
    }

    public ValidationList<ElectricityPriceStructureAndRuleAndSeasonReqVO> getPriceStructureAndRuleAndSeasonReqVOList() {
        return priceStructureAndRuleAndSeasonReqVOList;
    }

    public void setPriceStructureAndRuleAndSeasonReqVOList(ValidationList<ElectricityPriceStructureAndRuleAndSeasonReqVO> priceStructureAndRuleAndSeasonReqVOList) {
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