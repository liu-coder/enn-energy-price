package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:36
 */
@ApiModel("默认的体系响应VO")
public class ElectricityPriceDefaultStructureAndRuleRespVO implements Serializable {

    private static final long serialVersionUID = 3052881996864261585L;

    private ElectricityPriceStructureRuleDetailForCreateRespVO structureRuleDetailForCreateRespVO;

    private List<ElectricityPriceDetailForCreateRespVO> priceDetailForCreateRespVOList;

    public ElectricityPriceStructureRuleDetailForCreateRespVO getStructureRuleDetailForCreateRespVO() {
        return structureRuleDetailForCreateRespVO;
    }

    public void setStructureRuleDetailForCreateRespVO(ElectricityPriceStructureRuleDetailForCreateRespVO structureRuleDetailForCreateRespVO) {
        this.structureRuleDetailForCreateRespVO = structureRuleDetailForCreateRespVO;
    }

    public List<ElectricityPriceDetailForCreateRespVO> getPriceDetailForCreateRespVOList() {
        return priceDetailForCreateRespVOList;
    }

    public void setPriceDetailForCreateRespVOList(List<ElectricityPriceDetailForCreateRespVO> priceDetailForCreateRespVOList) {
        this.priceDetailForCreateRespVOList = priceDetailForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceDefaultStructureAndRuleRespVO{" +
                "structureRuleDetailForCreateRespVO=" + structureRuleDetailForCreateRespVO +
                ", priceDetailForCreateRespVOList=" + priceDetailForCreateRespVOList +
                '}';
    }
}
