package com.enn.energy.price.web.vo.requestvo;

import cn.hutool.core.date.DatePattern;
import com.enn.energy.price.web.validator.DateValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 15:28
 */
@ApiModel("电价分时区间vo")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElectricityTimeSectionUpdateReqVO implements Serializable {

    private static final long serialVersionUID = -7030440456697944470L;
    @ApiModelProperty(value = "分时区间id,新增的不传,修改的传")
    private Long id;
    @ApiModelProperty(value = "分时区间名称",required = true)
    @NotBlank(message = "分时区间名称不能为空")
    private String periods;
    @ApiModelProperty(value = "分时区间开始时间,时间格式HH:mm",required = true)
    @NotNull(message = "分时区间开始时间不能为空")
    @DateValue(format = "HH:mm", message = "分时区间开始时间格式有误")
    private String startTime;
    @ApiModelProperty(value = "分时区间结束时间,时间格式HH:mm",required = true)
    @NotBlank(message = "分时区间结束不能为空")
    @DateValue(format = "HH:mm", message = "分时区间结束时间格式有误")
    private String endTime;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;
}
