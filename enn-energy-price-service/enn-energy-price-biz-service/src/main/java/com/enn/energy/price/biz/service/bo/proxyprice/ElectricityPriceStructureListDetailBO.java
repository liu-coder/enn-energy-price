package com.enn.energy.price.biz.service.bo.proxyprice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:36
 */
public class ElectricityPriceStructureListDetailBO implements Serializable {

    private static final long serialVersionUID = 5435303555335291287L;

    private String lastVersionId;

    List<ElectricityPriceStructureDetailBO> structureDetailBOList;

    public String getLastVersionId() {
        return lastVersionId;
    }

    public void setLastVersionId(String lastVersionId) {
        this.lastVersionId = lastVersionId;
    }

    public List<ElectricityPriceStructureDetailBO> getStructureDetailBOList() {
        return structureDetailBOList;
    }

    public void setStructureDetailBOList(List<ElectricityPriceStructureDetailBO> structureDetailBOList) {
        this.structureDetailBOList = structureDetailBOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureListDetailBO{" +
                "lastVersionId='" + lastVersionId + '\'' +
                ", structureDetailBOList=" + structureDetailBOList +
                '}';
    }
}
