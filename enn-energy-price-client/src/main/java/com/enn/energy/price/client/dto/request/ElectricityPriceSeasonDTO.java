package com.enn.energy.price.client.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 电价季节DTO.
 *
 * @author : wuchaon
 * @version : 1.0 2021/12/1 13:50
 * @since : 1.0
 **/
@Data
public class ElectricityPriceSeasonDTO implements Serializable, Comparable<ElectricityPriceSeasonDTO> {

    private static final long serialVersionUID = 4079979966872626590L;

    /**
     * 定价方式
     */
    @ApiModelProperty(value = "定价方式策略", example = "", required = true)
    @NotBlank(message = "定价方式必填")
    @Length(max=20,message = "定价方式的长度不能超过20")
    private String pricingMethod;

    /**
     * 季节开始时间
     */
    @ApiModelProperty(value = "季节开始时间", example = "11-22,只包括日期", required = true)
    @NotBlank(message = "季节开始时间必填")
    @Length(max=5,message = "季节开始时间长度不能超过5")
    private String seaStartDate;

    /**
     * 季节结束时间
     */
    @ApiModelProperty(value = "季节结束时间", example = "11-22,只包括日期", required = true)
    @NotBlank(message = "季节结束时间必填")
    @Length(max=5,message = "季节结束时间的长度不能超过5")
    private String seaEndDate;

    /**
     * 季节名称
     */
    @ApiModelProperty(value = "季节名称", example = "春季", required = true)
    @NotBlank(message = "季节名称必填")
    @Length(max=20,message = "季节名称的长度不能超过20")
    private String season;

    /**
     * 电价规则明细
     */
    @ApiModelProperty(value = "电价规则明细", required = true)
    @NotNull(message = "电价规则明细不能为空")
    @NotEmpty(message = "电价规则明细不能为空")
    @Valid
    private List<ElectricityPriceDetailDTO> electricityPriceDetailDTOList;


    @Override
    public int compareTo(ElectricityPriceSeasonDTO o) {
        return this.seaStartDate.compareTo(o.getSeaStartDate());
    }
}
