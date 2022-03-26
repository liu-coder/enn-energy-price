package com.enn.energy.price.client.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 电价版本DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 15:53
 * @since : 1.0
 **/
@Data
@ApiModel(description = "罗森电价同步入参")
public class ElectricityPriceVersionDTO implements Serializable {

    private static final long serialVersionUID = 3328648947555819752L;

    /**
     * 电价版本名称
     */
    @ApiModelProperty(value = "电价版本名称", required = true)
    @NotBlank(message = "电价版本名称必填")
    @Length(max=30,message = "电价版本长度不能超过30")
    private String versionName;

    /**
     * 省编码
     */
    @Length(max=20,message = "省编码长度不能超过20")
    private String provinceCode;

    /**
     * 省
     */
    @Length(max=20,message = "省名称长度不能超过20")
    private String province;

    /**
     * 市编码
     */
    @Length(max=20,message = "市编码长度不能超过20")
    private String cityCode;

    /**
     * 市
     */
    @Length(max=20,message = "市名称长度不能超过20")
    private String city;

    /**
     * 区、县编码
     */
    @Length(max=20,message = "区、县编码长度不能超过20")
    private String districtCode;

    /**
     * 区、县
     */
    @Length(max=20,message = "区、县名称长度不能超过20")
    private String district;

    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码", required = true)
    @NotBlank(message = "系统编码必填")
    @Length(max=20,message = "系统编码长度不能超过20")
    private String systemCode;

    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称", required = true)
    @NotBlank(message = "系统名称必填")
    @Length(max=20,message = "系统名称长度不能超过20")
    private String systemName;

    /**
     * 企业租户id
     */
    @ApiModelProperty(value = "企业租户id", required = true)
    @NotBlank(message = "企业租户id必填")
    @Length(max=20,message = "企业租户id长度不能超过20")
    private String tenantId;

    /**
     * 企业租户名称
     */
    @ApiModelProperty(value = "企业租户名称", required = true)
    @NotBlank(message = "企业租户名称必填")
    @Length(max=20,message = "企业租户名称长度不能超过20")
    private String tenantName;

    /**
     * 设备
     */
    @ApiModelProperty(value = "设备", required = true)
    @NotNull(message = "设备信息必填")
    @Valid
    private ElectricityPriceEquipmentDTO electricityPriceEquipmentDTO;

    /**
     * 生效日期
     */
    @ApiModelProperty(value = "生效日期", example = "2021-11-19", required = true)
    @NotNull(message = "生效日期必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Future(message = "生效日期必须大于今天")
    private Date startDate;

    /**
     * 失效日期
     */
//    @ApiModelProperty(value = "失效日期", example = "2021-11-19", required = true)
//    @NotNull(message = "失效日期必填")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd")
////    @Future(message = "失效日期必须大于今天")
//    private Date endDate;

    /**
     * 电价类型,0:目录电价;1:自定义电价
     */
    @ApiModelProperty(value = "电价类型", example = "0:目录电价;1:自定义电价", required = true)
    @NotBlank(message = "电价类型必填")
    @Length(max=4, message = "电价类型长度不能超过4")
    private String priceType;

    /**
     * 绑定类型
     */
    @ApiModelProperty(value = "绑定类型", example = "0:企业;1:设备;2:行政区域", required = true)
    @NotBlank(message = "绑定类型必填")
    @Length(max=4,message = "绑定类型长度不能超过4")
    private String bindType;

    /**
     * 版本对应的规则
     */
    @ApiModelProperty(value = "版本对应的规则", required = true)
    @NotNull(message = "版本对应的规则不能为空")
    @NotEmpty(message = "版本对应的规则不能为空")
    @Valid
    private List<ElectricityPriceRuleDTO> electricityPriceRuleDTOList;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String commodityCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String commodityName;

}
