package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @date 2022/5/5
 */
@ApiModel("季节分时以及电价规则校验响应VO")
public class ElectricityPriceStructureAndRuleValidateRespVO implements Serializable {

    private static final long serialVersionUID = -3178172683015216037L;

    @ApiModelProperty(value = "是否存在重复的季节对应的体系三要素组合,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistDuplicatedStructureRule;

    @ApiModelProperty(value = "是否存在季节区间时间段存在交集,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistIntersectionSeasonSectionRule;

    @ApiModelProperty(value = "存在季节区间时间段未达到一年时,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifNotReachWholeYearSeasonSection;

    @ApiModelProperty(value = "存在分时区间时间段存在交集,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistIntersectionTimeSection;

    @ApiModelProperty(value = "存在分时区间时间段未达到24时,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifNotReachWholeDayTimeSection;

    @ApiModelProperty(value = "存在完全相同的修正策略行号集合,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistSameTimeSection;



    @ApiModelProperty(value = "是否存在重复的体系三要素,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistDuplicatedRule;

    @ApiModelProperty(value = "已绑定设备，不能修改的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> cantEditPriceRules;

    @ApiModelProperty(value = "用电分类值不合法的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> illegalIndustry;

    @ApiModelProperty(value = "电价类型值不合法的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> illegalStrategy;

    @ApiModelProperty(value = "电压等级值不合法的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> illegalVoltageLevel;

    @ApiModelProperty(value = "当为两部制时，缺少容需量用电价格的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> lackCapacityAndDemandCapacityPriceList;

    @ApiModelProperty(value = "当为一部制时，无需容需量用电价格的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> needLessCapacityAndDemandCapacityPriceList;

    @ApiModelProperty(value = "缺少电度电价的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> lackConsumptionPriceList;

    @ApiModelProperty(value = "缺少分时电价的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> lackTimePartPriceList;

    @ApiModelProperty(value = "不合法的峰阶段电价行号集合", dataType = "list")
    private List<String> illegalPeekList;

    @ApiModelProperty(value = "不合法的尖阶段电价行号集合", dataType = "list")
    private List<String> illegalSharpList;

    @ApiModelProperty(value = "不合法的平阶段电价行号集合", dataType = "list")
    private List<String> illegalLevelList;

    @ApiModelProperty(value = "不合法的谷阶段电价行号集合", dataType = "list")
    private List<String> illegalValleyList;

    @ApiModelProperty(value = "不合法的政府附加电价行号集合", dataType = "list")
    private List<String> illegalGovAddPriceList;

    @ApiModelProperty(value = "不合法的电度输配电价行号集合", dataType = "list")
    private List<String> illegalDistributionPriceList;

    @ApiModelProperty(value = "不合法的电度用电价格行号集合", dataType = "list")
    private List<String> illegalConsumptionPriceList;

    @ApiModelProperty(value = "不合法的最大容量基础电价行号集合", dataType = "list")
    private List<String> illegalMaxCapacityPriceList;

    @ApiModelProperty(value = "不合法的变压器容量基础电价行号集合", dataType = "list")
    private List<String> illegalTransformerCapacityPriceList;

    public Boolean getIfExistDuplicatedStructureRule() {
        return ifExistDuplicatedStructureRule;
    }

    public void setIfExistDuplicatedStructureRule(Boolean ifExistDuplicatedStructureRule) {
        this.ifExistDuplicatedStructureRule = ifExistDuplicatedStructureRule;
    }



    public Boolean getIfExistDuplicatedRule() {
        return ifExistDuplicatedRule;
    }

    public void setIfExistDuplicatedRule(Boolean ifExistDuplicatedRule) {
        this.ifExistDuplicatedRule = ifExistDuplicatedRule;
    }

    public List<String> getIllegalIndustry() {
        return illegalIndustry;
    }

    public void setIllegalIndustry(List<String> illegalIndustry) {
        this.illegalIndustry = illegalIndustry;
    }

    public List<String> getIllegalStrategy() {
        return illegalStrategy;
    }

    public void setIllegalStrategy(List<String> illegalStrategy) {
        this.illegalStrategy = illegalStrategy;
    }

    public List<String> getIllegalVoltageLevel() {
        return illegalVoltageLevel;
    }

    public void setIllegalVoltageLevel(List<String> illegalVoltageLevel) {
        this.illegalVoltageLevel = illegalVoltageLevel;
    }

    public List<String> getLackCapacityAndDemandCapacityPriceList() {
        return lackCapacityAndDemandCapacityPriceList;
    }

    public void setLackCapacityAndDemandCapacityPriceList(List<String> lackCapacityAndDemandCapacityPriceList) {
        this.lackCapacityAndDemandCapacityPriceList = lackCapacityAndDemandCapacityPriceList;
    }

    public List<String> getNeedLessCapacityAndDemandCapacityPriceList() {
        return needLessCapacityAndDemandCapacityPriceList;
    }

    public void setNeedLessCapacityAndDemandCapacityPriceList(List<String> needLessCapacityAndDemandCapacityPriceList) {
        this.needLessCapacityAndDemandCapacityPriceList = needLessCapacityAndDemandCapacityPriceList;
    }

    public List<String> getLackTimePartPriceList() {
        return lackTimePartPriceList;
    }

    public void setLackTimePartPriceList(List<String> lackTimePartPriceList) {
        this.lackTimePartPriceList = lackTimePartPriceList;
    }

    public List<String> getCantEditPriceRules() {
        return cantEditPriceRules;
    }

    public void setCantEditPriceRules(List<String> cantEditPriceRules) {
        this.cantEditPriceRules = cantEditPriceRules;
    }


    public List<String> getIllegalPeekList() {
        return illegalPeekList;
    }

    public void setIllegalPeekList(List<String> illegalPeekList) {
        this.illegalPeekList = illegalPeekList;
    }

    public List<String> getIllegalSharpList() {
        return illegalSharpList;
    }

    public void setIllegalSharpList(List<String> illegalSharpList) {
        this.illegalSharpList = illegalSharpList;
    }

    public List<String> getIllegalLevelList() {
        return illegalLevelList;
    }

    public void setIllegalLevelList(List<String> illegalLevelList) {
        this.illegalLevelList = illegalLevelList;
    }

    public List<String> getIllegalValleyList() {
        return illegalValleyList;
    }

    public void setIllegalValleyList(List<String> illegalValleyList) {
        this.illegalValleyList = illegalValleyList;
    }

    public List<String> getIllegalGovAddPriceList() {
        return illegalGovAddPriceList;
    }

    public void setIllegalGovAddPriceList(List<String> illegalGovAddPriceList) {
        this.illegalGovAddPriceList = illegalGovAddPriceList;
    }

    public List<String> getIllegalDistributionPriceList() {
        return illegalDistributionPriceList;
    }

    public void setIllegalDistributionPriceList(List<String> illegalDistributionPriceList) {
        this.illegalDistributionPriceList = illegalDistributionPriceList;
    }

    public List<String> getIllegalConsumptionPriceList() {
        return illegalConsumptionPriceList;
    }

    public void setIllegalConsumptionPriceList(List<String> illegalConsumptionPriceList) {
        this.illegalConsumptionPriceList = illegalConsumptionPriceList;
    }

    public List<String> getIllegalMaxCapacityPriceList() {
        return illegalMaxCapacityPriceList;
    }

    public void setIllegalMaxCapacityPriceList(List<String> illegalMaxCapacityPriceList) {
        this.illegalMaxCapacityPriceList = illegalMaxCapacityPriceList;
    }

    public List<String> getIllegalTransformerCapacityPriceList() {
        return illegalTransformerCapacityPriceList;
    }

    public void setIllegalTransformerCapacityPriceList(List<String> illegalTransformerCapacityPriceList) {
        this.illegalTransformerCapacityPriceList = illegalTransformerCapacityPriceList;
    }

    public Boolean getIfExistIntersectionSeasonSectionRule() {
        return ifExistIntersectionSeasonSectionRule;
    }

    public void setIfExistIntersectionSeasonSectionRule(Boolean ifExistIntersectionSeasonSectionRule) {
        this.ifExistIntersectionSeasonSectionRule = ifExistIntersectionSeasonSectionRule;
    }

    public Boolean getIfNotReachWholeYearSeasonSection() {
        return ifNotReachWholeYearSeasonSection;
    }

    public void setIfNotReachWholeYearSeasonSection(Boolean ifNotReachWholeYearSeasonSection) {
        this.ifNotReachWholeYearSeasonSection = ifNotReachWholeYearSeasonSection;
    }

    public Boolean getIfExistIntersectionTimeSection() {
        return ifExistIntersectionTimeSection;
    }

    public void setIfExistIntersectionTimeSection(Boolean ifExistIntersectionTimeSection) {
        this.ifExistIntersectionTimeSection = ifExistIntersectionTimeSection;
    }

    public Boolean getIfNotReachWholeDayTimeSection() {
        return ifNotReachWholeDayTimeSection;
    }

    public void setIfNotReachWholeDayTimeSection(Boolean ifNotReachWholeDayTimeSection) {
        this.ifNotReachWholeDayTimeSection = ifNotReachWholeDayTimeSection;
    }

    public Boolean getIfExistSameTimeSection() {
        return ifExistSameTimeSection;
    }

    public void setIfExistSameTimeSection(Boolean ifExistSameTimeSection) {
        this.ifExistSameTimeSection = ifExistSameTimeSection;
    }

    public List<String> getLackConsumptionPriceList() {
        return lackConsumptionPriceList;
    }

    public void setLackConsumptionPriceList(List<String> lackConsumptionPriceList) {
        this.lackConsumptionPriceList = lackConsumptionPriceList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureAndRuleValidateRespVO{" +
                "ifExistDuplicatedStructureRule=" + ifExistDuplicatedStructureRule +
                ", ifExistIntersectionSeasonSectionRule=" + ifExistIntersectionSeasonSectionRule +
                ", ifNotReachWholeYearSeasonSection=" + ifNotReachWholeYearSeasonSection +
                ", ifExistIntersectionTimeSection=" + ifExistIntersectionTimeSection +
                ", ifNotReachWholeDayTimeSection=" + ifNotReachWholeDayTimeSection +
                ", ifExistSameTimeSection=" + ifExistSameTimeSection +
                ", ifExistDuplicatedRule=" + ifExistDuplicatedRule +
                ", cantEditPriceRules=" + cantEditPriceRules +
                ", illegalIndustry=" + illegalIndustry +
                ", illegalStrategy=" + illegalStrategy +
                ", illegalVoltageLevel=" + illegalVoltageLevel +
                ", lackCapacityAndDemandCapacityPriceList=" + lackCapacityAndDemandCapacityPriceList +
                ", needLessCapacityAndDemandCapacityPriceList=" + needLessCapacityAndDemandCapacityPriceList +
                ", lackConsumptionPriceList=" + lackConsumptionPriceList +
                ", lackTimePartPriceList=" + lackTimePartPriceList +
                ", illegalPeekList=" + illegalPeekList +
                ", illegalSharpList=" + illegalSharpList +
                ", illegalLevelList=" + illegalLevelList +
                ", illegalValleyList=" + illegalValleyList +
                ", illegalGovAddPriceList=" + illegalGovAddPriceList +
                ", illegalDistributionPriceList=" + illegalDistributionPriceList +
                ", illegalConsumptionPriceList=" + illegalConsumptionPriceList +
                ", illegalMaxCapacityPriceList=" + illegalMaxCapacityPriceList +
                ", illegalTransformerCapacityPriceList=" + illegalTransformerCapacityPriceList +
                '}';
    }
}