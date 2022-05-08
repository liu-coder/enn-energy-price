package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 16:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典响应vo")
public class ElectricityPriceDictionaryVO implements Serializable {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("key值")
    private String code;
    @ApiModelProperty("value值")
    private String name;
    @ApiModelProperty("type")
    private Integer type;
    @ApiModelProperty("类型描述")
    private String typeDesc;
    @ApiModelProperty("是否可用状态， 默认0")
    private Integer state;
}
