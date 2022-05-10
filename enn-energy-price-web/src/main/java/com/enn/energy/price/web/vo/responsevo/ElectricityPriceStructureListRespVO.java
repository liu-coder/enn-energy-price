package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sunjidong
 * @version 1.0.0
 * @Date 2022/5/8 17:36
 */
public class ElectricityPriceStructureListRespVO implements Serializable {

    private static final long serialVersionUID = 7664192924328097571L;

    List<ElectricityPriceStructureDetailForCreateRespVO> structureDetailForCreateRespVOList;

    public List<ElectricityPriceStructureDetailForCreateRespVO> getStructureDetailForCreateRespVOList() {
        return structureDetailForCreateRespVOList;
    }

    public void setStructureDetailForCreateRespVOList(List<ElectricityPriceStructureDetailForCreateRespVO> structureDetailForCreateRespVOList) {
        this.structureDetailForCreateRespVOList = structureDetailForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureListRespVO{" +
                "structureDetailForCreateRespVOList=" + structureDetailForCreateRespVOList +
                '}';
    }
}
