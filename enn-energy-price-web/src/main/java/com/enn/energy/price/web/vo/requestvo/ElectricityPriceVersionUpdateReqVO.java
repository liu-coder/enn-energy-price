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
import java.io.Serializable;
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
    private String id;
    @ApiModelProperty(value = "版本名",required = true)
    @NotBlank(message = "版本名不能为空")
    @Length(max = 50,message = "电价体系名称最长50个字符")
    private String versionName;
    @ApiModelProperty(value = "省编码",required = true)
    @NotBlank(message = "省编码不能为空")
    private String provinceCode;
    @ApiModelProperty(value = "电价版本开始时间,时间格式yyyy-MM-dd",required = true)
    @NotBlank(message = "电价版本开始时间不能为空")
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "电价版本开始时间格式有误")
    private String startDate;
    @ApiModelProperty(value = "电价版本结束时间,时间格式yyyy-MM-dd",required = true)
    @DateValue(format = DatePattern.NORM_DATE_PATTERN, message = "电价版本结束时间格式有误")
    private String endDate;
    @ApiModelProperty(value = "请求时间(yyyy-MM-dd HH:mm:ss)",required = true)
    @NotBlank(message = "请求时间戳不能为空")
    @DateValue(format = DatePattern.NORM_DATETIME_PATTERN, message = "请求时间戳格式有误")
    private String timestamp;
    @ApiModelProperty(value = "父版本id", required = true, dataType = "string")
    private String lastVersionId;
    @ApiModelProperty(value = "系统编码", required = true, dataType = "string")
    private String systemCode;
    @ApiModelProperty(value = "系统名称", required = true, dataType = "string")
    private String systemName;
    @ApiModelProperty(value = "电价类型,0:目录电价;1:自定义电价;2:代购电价", required = true, dataType = "string")
    @NotBlank(message = "电价类型不能为空")
    private String priceType;
    @ApiModelProperty(value = "绑定类型,0:企业;1:设备;2:行政区域", required = true, dataType = "string")
    private String bindType;
    @ApiModelProperty(value = "电价体系列表",required = true)
    @NotEmpty(message = "电价体系不能为空")
    private List<@Valid ElectricityPriceStructureUpdateReqVO> electricityPriceStructureUpdateReqVOList;

}
