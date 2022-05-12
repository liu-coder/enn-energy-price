package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/12 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("省vo")
public class ProvinceVO implements Serializable {
    @ApiModelProperty(value = "省id",required = false)
    private String id;
    @ApiModelProperty(value = "省编码",required = false)
    private String areaCode;
    @ApiModelProperty(value = "省名称",required = false)
    private String name;
}
