package com.enn.energy.price.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/23 9:07
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceDictionarySelectRespDTO implements Serializable {

    private List<ElectricityPriceDictionarySelectItemResp> items;
    @Data
    public static class ElectricityPriceDictionarySelectItemResp implements Serializable{
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
        private Date createTime;
        private Date updateTime;
    }
}
