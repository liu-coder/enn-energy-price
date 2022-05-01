package com.enn.energy.price.web.vo.requestvo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 14:53
 */
@ApiModel("电价季节分时请求vo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 6708324781222848894L;
    @ApiModelProperty(value = "季节id,新增的不传,修改的需要")
    private Integer seasonId;
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
    @ApiModelProperty(value = "季节开始时间",required = true)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "季节开始时间不能为空")
    private Date seaStartDate;
    @ApiModelProperty(value ="季节结束时间",required = true )
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "季节结束时间不能为空")
    private Date seaEndDate;
    @ApiModelProperty(value = "修正策略列表")
    @NotEmpty(message = "修正策略列表不能为空")
    @Valid
    private List<ElectricityPriceStrategyReqVO> electricityPriceStrategyReqVOList;


}
