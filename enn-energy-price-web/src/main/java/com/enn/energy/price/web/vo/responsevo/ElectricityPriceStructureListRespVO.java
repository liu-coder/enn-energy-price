package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("所有体系以及体系对应的详细配置内容响应VO")
public class ElectricityPriceStructureListRespVO implements Serializable {

    private static final long serialVersionUID = 7664192924328097571L;

    @ApiModelProperty(value = "父版本id",dataType = "list")
    private String lastVersionId;

    @ApiModelProperty(value = "所有体系以及体系对应的详细配置内容",dataType = "list")
    List<ElectricityPriceStructureDetailRespVO> structureDetailForCreateRespVOList;

    public String getLastVersionId() {
        return lastVersionId;
    }

    public void setLastVersionId(String lastVersionId) {
        this.lastVersionId = lastVersionId;
    }

    public List<ElectricityPriceStructureDetailRespVO> getStructureDetailForCreateRespVOList() {
        return structureDetailForCreateRespVOList;
    }

    public void setStructureDetailForCreateRespVOList(List<ElectricityPriceStructureDetailRespVO> structureDetailForCreateRespVOList) {
        this.structureDetailForCreateRespVOList = structureDetailForCreateRespVOList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceStructureListRespVO{" +
                "lastVersionId='" + lastVersionId + '\'' +
                ", structureDetailForCreateRespVOList=" + structureDetailForCreateRespVOList +
                '}';
    }
}
