package com.enn.energy.price.dal.po.view;

import java.util.Date;

/**
 * @author     ：chenchangtong
 * @date       ：Created 2021/12/10 13:38
 * @description：快乐工作每一天
 */
public class ElectricityPriceEquVersionView {
    private String versionId;
    private String ruleId;
    private Date startDate;
    private Date endDate;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
