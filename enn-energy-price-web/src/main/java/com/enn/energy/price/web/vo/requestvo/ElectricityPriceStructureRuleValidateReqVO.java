package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 季节分时体系请求VO
 *
 * @author sunjidong
 * @date 2022/4/30
 **/
@ApiModel("季节分时对应的体系请求VO")
public class ElectricityPriceStructureRuleValidateReqVO implements Serializable {

    private static final long serialVersionUID = -5758432421217592203L;

    @ApiModelProperty(value = "主键id", required = false, dataType = "string")
    private Long id;

    @ApiModelProperty(value = "体系id", required = false, dataType = "string")
    private String structureId;

    @ApiModelProperty(value = "体系规则id", required = false, dataType = "string")
    private String structureRuleId;

    @ApiModelProperty(value = "用电行业", required = true, dataType = "string")
    @NotBlank(message = "用电行业不能为空,多个使用,拼接")
    private String industries;

    @ApiModelProperty(value = "定价策略，0:单一制;1:双部制", required = true, dataType = "string")
    @NotBlank(message = "定价策略不能为空,多个使用,拼接")
    private String strategies;

    @ApiModelProperty(value = "电压等级", required = true, dataType = "string")
    @NotBlank(message = "电压等级不能为空,多个使用,拼接")
    private String voltageLevels;

    @NotEmpty(message = "季节不能为空")
    @Valid
    private ValidationList<ElectricitySeasonValidateReqVO> seasonValidateReqVOList;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureRuleId() {
        return structureRuleId;
    }

    public void setStructureRuleId(String structureRuleId) {
        this.structureRuleId = structureRuleId;
    }

    public ValidationList<ElectricitySeasonValidateReqVO> getSeasonValidateReqVOList() {
        return seasonValidateReqVOList;
    }

    public void setSeasonValidateReqVOList(ValidationList<ElectricitySeasonValidateReqVO> seasonValidateReqVOList) {
        this.seasonValidateReqVOList = seasonValidateReqVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureRuleValidateReqVO{" +
                "id=" + id +
                ", structureId='" + structureId + '\'' +
                ", structureRuleId='" + structureRuleId + '\'' +
                ", industries='" + industries + '\'' +
                ", strategies='" + strategies + '\'' +
                ", voltageLevels='" + voltageLevels + '\'' +
                ", seasonValidateReqVOList=" + seasonValidateReqVOList +
                '}';
    }
}