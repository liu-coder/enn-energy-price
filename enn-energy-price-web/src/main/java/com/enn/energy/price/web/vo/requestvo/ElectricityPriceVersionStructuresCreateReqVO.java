package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import com.enn.energy.price.web.validator.DateValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @ApiModelProperty(value = "请求时间(yyyy-MM-dd HH:mm:ss)",required = true)
    @NotBlank(message = "请求时间不能为空")
    @DateValue(format = {"yyyy-MM-dd HH:mm:ss"},message = "请求时间戳格式有误")
    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ElectricityPriceVersionStructuresCreateReqVO{" +
                "priceVersionCreateReqVO=" + priceVersionCreateReqVO +
                ", priceStructureAndRuleAndSeasonCreateReqVOList=" + priceStructureAndRuleAndSeasonCreateReqVOList +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}