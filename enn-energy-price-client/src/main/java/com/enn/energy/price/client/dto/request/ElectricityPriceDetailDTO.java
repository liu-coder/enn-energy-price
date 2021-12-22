package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 电价规则明细DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/19 16:29
 * @since : 1.0
 **/
@Data
public class ElectricityPriceDetailDTO implements Serializable,Comparable<ElectricityPriceDetailDTO>  {

    private static final long serialVersionUID = 680834680137302654L;

    /**
     * 电价明细id
     */
    //private String detailId;

    /**
     * 时段，0:尖;1:峰;2:平;3:谷
     */
    @ApiModelProperty(value = "时段", example = "0:尖;1:峰;2:平;3:谷")
    //@NotBlank(message = "时段必填")
    @Length(max=4, message = "时段长度不能超过4")
    private String periods;

    /**
     * 时段开始时间
     */
    @ApiModelProperty(value = "开始时间", example = "11:00，到小时")
    //@NotBlank(message = "明细开始时间必填")
    @Length(max=5, message = "时段开始时间不能超过5")
    private String startTime;

    /**
     * 时段结束时间
     */
    @ApiModelProperty(value = "结束时间", example = "11:00，到小时")
   // @NotBlank(message = "明细结束时间必填")
    @Length(max=5,message = "时段结束时间不能超过5")
    private String endTime;

    /**
     * 阶梯定义
     */
    @Length(max=20,message = "阶梯定义长度不能超过20")
    private String step;

    /**
     * 阶梯起码
     */
    @Length(max=20,message = "阶梯起码长度不能超过20")
    private String startStep;

    /**
     * 阶梯止码
     */
    @Length(max=20,message = "阶梯止码长度不能超过20")
    private String endStep;

    /**
     * 电价
     */
    @ApiModelProperty(value = "电价", required = true)
    @NotBlank(message = "电价必填")
    @Pattern(regexp="^([0-9]+[.]?[0-9]*)$", message = "价格必须为数字")
    @Length(max=20,message = "价格长度不能超过20")
    private String price;

    @Override
    public int compareTo(ElectricityPriceDetailDTO o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
