package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
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
public class ElectricityPriceVersionStructuresCreateReqVO implements Serializable {

    private static final long serialVersionUID = -9169380011531279714L;

    @NotNull(message = "新增版本不能为空")
    @Valid
    private ElectricityPriceVersionCreateReqVO priceVersionCreateReqVO;

    @NotEmpty(message = "新增体系的内容不能为空")
    @Valid
    private ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateReqVO> priceStructureAndRuleAndSeasonCreateReqVOList;

    public ElectricityPriceVersionCreateReqVO getPriceVersionCreateReqVO() {
        return priceVersionCreateReqVO;
    }

    public void setPriceVersionCreateReqVO(ElectricityPriceVersionCreateReqVO priceVersionCreateReqVO) {
        this.priceVersionCreateReqVO = priceVersionCreateReqVO;
    }

    public ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateReqVO> getPriceStructureAndRuleAndSeasonCreateReqVOList() {
        return priceStructureAndRuleAndSeasonCreateReqVOList;
    }

    public void setPriceStructureAndRuleAndSeasonCreateReqVOList(ValidationList<ElectricityPriceStructureAndRuleAndSeasonCreateReqVO> priceStructureAndRuleAndSeasonCreateReqVOList) {
        this.priceStructureAndRuleAndSeasonCreateReqVOList = priceStructureAndRuleAndSeasonCreateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceVersionStructuresCreateReqVO{" +
                "priceVersionCreateReqVO=" + priceVersionCreateReqVO +
                ", priceStructureAndRuleAndSeasonCreateReqVOList=" + priceStructureAndRuleAndSeasonCreateReqVOList +
                '}';
    }
}