package com.enn.energy.price.biz.service.bo.proxyprice;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * xxxxx此处为类的描述信息
 *
 * @author sunjidong
 * @date 2022/5/7
 **/
public class StructureRuleAndTimeTypeBO implements Serializable {

    private static final long serialVersionUID = 455529452021350696L;

    private String industries;

    private String strategies;

    private String voltageLevels;

    private Set<String> timeTypeList;

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

    public Set<String> getTimeTypeList() {
        return timeTypeList;
    }

    public void setTimeTypeList(Set<String> timeTypeList) {
        this.timeTypeList = timeTypeList;
    }

    @Override
    public String toString() {
        return "StructureRuleAndTimeTypeBO{" +
                "industries='" + industries + '\'' +
                ", strategies='" + strategies + '\'' +
                ", voltageLevels='" + voltageLevels + '\'' +
                ", timeTypeList=" + timeTypeList +
                '}';
    }
}