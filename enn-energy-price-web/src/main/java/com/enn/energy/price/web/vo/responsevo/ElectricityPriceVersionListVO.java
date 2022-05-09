package com.enn.energy.price.web.vo.responsevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceVersionListVO implements Serializable {
    private static final long serialVersionUID = -5387315080247554553L;
    @ApiModelProperty("版本list")
    List<ElectricityPriceVersionVO> electricityPriceVersionVOList;
}
