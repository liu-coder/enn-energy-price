package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
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
public class ElectricityPriceStructureAndRuleAndSeasonCreateReqVO implements Serializable {

    @NotNull(message = "新增体系不能为空")
    @Valid
    private ElectricityPriceStructureCreateReqVO priceStructureCreateReqVO;

    @NotEmpty(message = "季节分时对应的体系不能为空")
    @Valid
    private ValidationList<ElectricityPriceStructureRuleCreateReqVO> priceStructureRuleCreateReqVOList;

    @NotEmpty(message = "电价规则不能为空")
    @Valid
    private ValidationList<ElectricityPriceRuleCreateReqVO> priceRuleCreateReqVOList;

    public ElectricityPriceStructureCreateReqVO getPriceStructureCreateReqVO() {
        return priceStructureCreateReqVO;
    }

    public void setPriceStructureCreateReqVO(ElectricityPriceStructureCreateReqVO priceStructureCreateReqVO) {
        this.priceStructureCreateReqVO = priceStructureCreateReqVO;
    }

    public ValidationList<ElectricityPriceStructureRuleCreateReqVO> getPriceStructureRuleCreateReqVOList() {
        return priceStructureRuleCreateReqVOList;
    }

    public void setPriceStructureRuleCreateReqVOList(ValidationList<ElectricityPriceStructureRuleCreateReqVO> priceStructureRuleCreateReqVOList) {
        this.priceStructureRuleCreateReqVOList = priceStructureRuleCreateReqVOList;
    }

    public ValidationList<ElectricityPriceRuleCreateReqVO> getPriceRuleCreateReqVOList() {
        return priceRuleCreateReqVOList;
    }

    public void setPriceRuleCreateReqVOList(ValidationList<ElectricityPriceRuleCreateReqVO> priceRuleCreateReqVOList) {
        this.priceRuleCreateReqVOList = priceRuleCreateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureAndRuleAndSeasonCreateReqVO{" +
                "priceStructureCreateReqVO=" + priceStructureCreateReqVO +
                ", priceStructureRuleCreateReqVOList=" + priceStructureRuleCreateReqVOList +
                ", priceRuleCreateReqVOList=" + priceRuleCreateReqVOList +
                '}';
    }
}