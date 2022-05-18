package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 取消区域请求VO
 *
 * @author sunjidong
 * @date 2022/5/17
 **/
@ApiModel("取消区域请求VO")
public class ElectricityPriceDeleteAreaValidateReqVo implements Serializable {

    private static final long serialVersionUID = -9004160393004861844L;

    @ApiModelProperty(value = "体系id", required = false, dataType = "string")
    private String structureId;

    @ApiModelProperty(value = "系适用区域", required = true, dataType = "list")
    @NotEmpty(message = "体系适用区域不能为空")
    private List<String> districtCodeList;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public List<String> getDistrictCodeList() {
        return districtCodeList;
    }

    public void setDistrictCodeList(List<String> districtCodeList) {
        this.districtCodeList = districtCodeList;
    }

    @Override
    public String toString() {
        return "ElectricityPriceDeleteAreaValidateReqVo{" +
                "structureId='" + structureId + '\'' +
                ", districtCodeList=" + districtCodeList +
                '}';
    }
}

