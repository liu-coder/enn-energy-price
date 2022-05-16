package com.enn.energy.price.biz.service.bo.proxyprice;

import java.io.Serializable;

/**
 * @description:电价解除绑定请求参数
 * @author:quyl
 * @createTime:2022/5/4 15:17
 */
public class ElectricityPriceBindRemoveBO implements Serializable {

    private Long id;

    private Integer nextChangeFlag;

    private Long nextId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNextChangeFlag() {
        return nextChangeFlag;
    }

    public void setNextChangeFlag(Integer nextChangeFlag) {
        this.nextChangeFlag = nextChangeFlag;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    @Override
    public String toString() {
        return "ElectricityPriceBindRemoveBO{" +
                "id=" + id +
                ", nextChangeFlag=" + nextChangeFlag +
                ", nextId=" + nextId +
                '}';
    }
}
