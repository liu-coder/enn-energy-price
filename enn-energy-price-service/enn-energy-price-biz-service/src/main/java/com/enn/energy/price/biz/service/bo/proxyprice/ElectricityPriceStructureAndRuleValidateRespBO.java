package com.enn.energy.price.biz.service.bo.proxyprice;

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

    private List<String> lackConsumptionPriceList;

    private List<String> lackTimePartPriceList;

    private List<String> noDeleteBillRulesList;

    private List<String> illegalPeekList;

    private List<String> illegalSharpList;

    private List<String> illegalLevelList;

    private List<String> illegalValleyList;

    private List<String> illegalGovAddPriceList;

    private List<String> illegalDistributionPriceList;

    private List<String> illegalConsumptionPriceList;

    private List<String> illegalMaxCapacityPriceList;

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
                ", lackConsumptionPriceList=" + lackConsumptionPriceList +
                ", lackTimePartPriceList=" + lackTimePartPriceList +
                ", noDeleteBillRulesList=" + noDeleteBillRulesList +
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