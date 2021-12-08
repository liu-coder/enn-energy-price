package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/22 16:06
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceDictionaryDTO {
    @NotNull(message = "字典key值必填")
    @ApiModelProperty("key值")
    private String key;
    @NotNull(message = "字典value值必填")
    @ApiModelProperty("value值")
    private String value;
    @NotNull(message = "常量类型必填")
    @ApiModelProperty("常量类型 1：电价类型 （目录电价、自定义电价）2：用电行业（行业key，行业名称）" +
            "3：电压等级（等级key，等级value）4：定价策略（单一制、双部制） 5：定价方式（单一电价、分时电价、阶梯电价）" +
            "6：分时类型（尖峰平谷）")
    private Integer type;
    @ApiModelProperty("类型描述")
    private String typeDesc;
//    @ApiModelProperty("是否删除状态。默认为0 状态 0-正常,1-已删除")
//    private Integer state = 0;
}
