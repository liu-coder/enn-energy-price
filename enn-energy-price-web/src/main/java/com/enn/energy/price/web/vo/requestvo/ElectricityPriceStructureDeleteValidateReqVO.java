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
 * @Date 2022/5/12 10:04
 */
@ApiModel("体系删除校验实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceStructureDeleteValidateReqVO implements Serializable {
    @ApiModelProperty("体系id")
    @NotBlank(message = "体系id不能为空")
    private String structureId;
    @ApiModelProperty("版本id")
    @NotBlank(message = "版本id不能为空")
    private String versionId;
    @ApiModelProperty("版本开始时间")
    @NotBlank(message = "版本开始时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "版本开始时间格式不对")
    private String startDate;
    @ApiModelProperty("版本结束时间")
    @NotBlank(message = "版本结束时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "版本结束时间格式不对")
    private String endDate;
}
