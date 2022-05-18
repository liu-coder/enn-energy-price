package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @author:quyl
 * @createTime:2022/5/11 7:25
 */
@ApiModel(description = "代理电价编辑绑定时详情请求对象")
@Data
public class ElectricityPriceBindEditReqVO implements Serializable {

    @ApiModelProperty(required = true, name = "价格节点绑定id")
    @NotNull(message = "价格节点绑定id不能为空")
    private Long id;

    private String nextChangeFlag;

    private Long nextId;
}
