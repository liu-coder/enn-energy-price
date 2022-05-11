package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 15:20
 */
@ApiModel("电价策略")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityPriceStrategyReqVO implements Serializable {
    private static final long serialVersionUID = 2172445294238207217L;
    @ApiModelProperty(value = "比较符 0:等于;-1:小于;1:大于")
    private String compare;
    @ApiModelProperty(value = "温度")
    private String temperature;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;
    @ApiModelProperty(value = "电价分时区间列表",required = true)
    @NotBlank(message = "电价分时区间列表不能为空")
    private List<ElectricityTimeSectionUpdateReqVO> electricityTimeSectionUpdateReqVOList;
}
