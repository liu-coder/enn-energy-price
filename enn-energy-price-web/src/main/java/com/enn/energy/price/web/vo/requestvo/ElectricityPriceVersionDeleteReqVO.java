package com.enn.energy.price.web.vo.requestvo;

import cn.hutool.core.date.DatePattern;
import com.enn.energy.price.web.validator.DateValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/6 21:32
 */
@ApiModel("电价版本删除请求vo")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityPriceVersionDeleteReqVO implements Serializable {
    private static final long serialVersionUID = -7291313346248713217L;
    @ApiModelProperty(value = "电价版本id",required = true)
    @NotBlank(message = "电价版本id不能为空")
    private String id;

    @ApiModelProperty(value = "省编码code", required = true, dataType = "string")
    @NotBlank(message = "省编码code不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "电价版本开始时间,时间格式yyyy-MM-dd",required = true)
    @NotBlank(message = "电价版本开始时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "电价版本开始时间格式有误")
    private String startDate;

    @ApiModelProperty(value = "电价版本结束时间,时间格式yyyy-MM-dd",required = true)
    @NotBlank(message = "电价版本结束时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "电价版本结束时间格式有误")
    private String endDate;
}
