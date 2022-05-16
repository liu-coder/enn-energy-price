package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@ApiModel(description = "代理电价解绑请求对象")
public class ElectricityPriceBindRemoveReqVO implements Serializable {

    @ApiModelProperty(required = true, name = "设备绑定id")
    @NotNull(message = "设备绑定id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否要自定义下一版本体系", example = "0:否 默认继承，1:是 自定义")
    @NotBlank(message = "是否要自定义下一版本体系标识必填")
    @Max(value = 1, message = "是否要自定义下一版本体系参数只能为0:否 默认继承，1:是 自定义")
    @Min(value = 0, message = "是否要自定义下一版本体系参数只能为0:否 默认继承，1:是 自定义")
    private Integer nextChangeFlag;

    @ApiModelProperty(name = "自定义体系时候下一个版本的设备绑定id")
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
        return "ElectricityPriceBindRemoveReqVO{" +
                "id=" + id +
                ", nextChangeFlag=" + nextChangeFlag +
                ", nextId=" + nextId +
                '}';
    }
}
