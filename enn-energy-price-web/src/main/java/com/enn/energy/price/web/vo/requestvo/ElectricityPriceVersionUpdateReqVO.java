package com.enn.energy.price.web.vo.requestvo;

import cn.hutool.core.date.DatePattern;
import com.enn.energy.price.web.validator.DateValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 10:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("电价版本修改vo")
public class ElectricityPriceVersionUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 211362895339802678L;
    @ApiModelProperty(value = "版本主键id",required = true )
    @NotNull(message = "主键id不能为空")
    private Integer id;
    @ApiModelProperty(value = "版本名",required = true)
    @NotBlank(message = "版本名不能为空")
    @Length(max = 50,message = "电价体系名称最长50个字符")
    private String versionName;
    private String province;
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;
    @ApiModelProperty(value = "租户名称",required = true)
    @NotBlank(message = "租户名称不能为空")
    private String tenantName;
    @ApiModelProperty(value = "电价版本开始时间,时间格式yyyy-MM-dd",required = true)
    @NotNull(message = "电价版本开始时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "电价版本开始时间格式有误")
    private String startDate;
    @ApiModelProperty(value = "电价版本结束时间,时间格式yyyy-MM-dd",required = true)
    @NotNull(message = "电价版本结束时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "电价版本结束时间格式有误")
    private String endDate;

    @ApiModelProperty(value = "电价体系列表",required = true)
    @NotEmpty(message = "电价体系不能为空")
    @Valid
    private List<ElectricityPriceStructureUpdateReqVO> electricityPriceStructureUpdateReqVOList;



}
