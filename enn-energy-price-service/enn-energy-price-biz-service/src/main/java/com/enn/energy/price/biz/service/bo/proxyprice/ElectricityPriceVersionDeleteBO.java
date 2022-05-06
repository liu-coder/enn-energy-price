package com.enn.energy.price.biz.service.bo.proxyprice;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/6 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceVersionDeleteBO implements Serializable {

    private static final long serialVersionUID = 3460840544312091475L;

    private String id;

    private String provinceCode;

    private String startDate;

    private String endDate;
}
