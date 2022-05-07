package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 季节分时以及电价规则校验结果返回BO
 * @author sunjidong
 * @date 2022/5/5
 */
public class ElectricityPriceStructureAndRuleValidateRespBO implements Serializable {

    private static final long serialVersionUID = 1494647417641921233L;

    private Boolean ifExistDuplicatedStructureRule;

    private Boolean ifExistIntersectionSeasonSectionRule;

    private Boolean ifNotReachWholeYearSeasonSection;

    private Boolean ifExistIntersectionTimeSection;

    private Boolean ifNotReachWholeDayTimeSection;

    private Boolean ifExistSameTimeSection;




    private Boolean ifExistDuplicatedRule;

    private List<String> cantEditPriceRules;

    private List<String> priceDigitExceed;

    private List<String> illegalIndustry;

    private List<String> illegalStrategy;

    private List<String> illegalVoltageLevel;

    private List<String> lackCapacityAndDemandCapacityPriceList;

    private List<String> needLessCapacityAndDemandCapacityPriceList;

    private List<String> lackDistributionPriceList;

    private List<String> lackTimePartPriceList;

    private List<String> noDeleteBillRulesList;

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

    public List<String> getPriceDigitExceed() {
        return priceDigitExceed;
    }

    public void setPriceDigitExceed(List<String> priceDigitExceed) {
        this.priceDigitExceed = priceDigitExceed;
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

    public List<String> getNoDeleteBillRulesList() {
        return noDeleteBillRulesList;
    }

    public void setNoDeleteBillRulesList(List<String> noDeleteBillRulesList) {
        this.noDeleteBillRulesList = noDeleteBillRulesList;
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
        return "ElectricityPriceStructureAndRuleValidateRespBO{" +
                "ifExistDuplicatedStructureRule=" + ifExistDuplicatedStructureRule +
                ", ifExistIntersectionSeasonSectionRule=" + ifExistIntersectionSeasonSectionRule +
                ", ifNotReachWholeYearSeasonSection=" + ifNotReachWholeYearSeasonSection +
                ", ifExistIntersectionTimeSection=" + ifExistIntersectionTimeSection +
                ", ifNotReachWholeDayTimeSection=" + ifNotReachWholeDayTimeSection +
                ", ifExistSameTimeSection=" + ifExistSameTimeSection +
                ", ifExistDuplicatedRule=" + ifExistDuplicatedRule +
                ", cantEditPriceRules=" + cantEditPriceRules +
                ", priceDigitExceed=" + priceDigitExceed +
                ", illegalIndustry=" + illegalIndustry +
                ", illegalStrategy=" + illegalStrategy +
                ", illegalVoltageLevel=" + illegalVoltageLevel +
                ", lackCapacityAndDemandCapacityPriceList=" + lackCapacityAndDemandCapacityPriceList +
                ", needLessCapacityAndDemandCapacityPriceList=" + needLessCapacityAndDemandCapacityPriceList +
                ", lackDistributionPriceList=" + lackDistributionPriceList +
                ", lackTimePartPriceList=" + lackTimePartPriceList +
                ", noDeleteBillRulesList=" + noDeleteBillRulesList +
                '}';
    }
}