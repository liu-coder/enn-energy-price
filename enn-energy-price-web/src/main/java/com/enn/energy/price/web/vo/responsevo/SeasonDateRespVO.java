package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.web.validator.DateValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/6 17:52
 */
@Data
@AllArgsConstructor
@Getter
public class SeasonDateRespVO implements Serializable {
    private static final long serialVersionUID = -7180031461745554430L;
    @ApiModelProperty(value = "季节开始时间,时间格式MM-dd",required = true)
    @DateValue(format = "MM-dd", message = "季节开始时间格式有误")
    @NotBlank(message = "季节开始时间不能为空")
    private String seaStartDate;
    @ApiModelProperty(value ="季节结束时间,时间格式MM-dd",required = true )
    @DateValue(format = "MM-dd", message = "季节结束时间格式有误")
    @NotBlank(message = "季节结束时间不能为空")
    private String seaEndDate;
    @ApiModelProperty(value = "季节id")
    private Long seasonId;
    @ApiModelProperty(value = "变更类型")
    private Integer changeType;
}
