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

    @ApiModelProperty(value = "是否存在季节区间时间段存在交集的,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistIntersectionSeasonSectionRule;

    @ApiModelProperty(value = "是否存在季节区间时间段未达到一年时的,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifNotReachWholeYearSeasonSection;

    @ApiModelProperty(value = "是否存在分时区间时间段存在交集的,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifExistIntersectionTimeSection;

    @ApiModelProperty(value = "是否存在分时区间时间段未达到24时的序号集合,true:存在，false：不存在", dataType = "boolean")
    private Boolean ifNotReachWholeDayTimeSection;

    @ApiModelProperty(value = "当存在修正策略时,true:存在，false：不存在", dataType = "boolean")
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
    private List<String> lackDistributionPriceList;

    @ApiModelProperty(value = "缺少分时电价的序号集合,当全部成功时，此为空集合", dataType = "list")
    private List<String> lackTimePartPriceList;



    public Boolean getIfExistDuplicatedStructureRule() {
        return ifExistDuplicatedStructureRule;
    }

    public void setIfExistDuplicatedStructureRule(Boolean ifExistDuplicatedStructureRule) {
        this.ifExistDuplicatedStructureRule = ifExistDuplicatedStructureRule;
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

    public List<String> getLackDistributionPriceList() {
        return lackDistributionPriceList;
    }

    public void setLackDistributionPriceList(List<String> lackDistributionPriceList) {
        this.lackDistributionPriceList = lackDistributionPriceList;
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

    public Boolean getIfExistSameTimeSection() {
        return ifExistSameTimeSection;
    }

    public void setIfExistSameTimeSection(Boolean ifExistSameTimeSection) {
        this.ifExistSameTimeSection = ifExistSameTimeSection;
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
                ", lackDistributionPriceList=" + lackDistributionPriceList +
                ", lackTimePartPriceList=" + lackTimePartPriceList +
                '}';
    }
}