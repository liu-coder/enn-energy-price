package com.enn.energy.price.web.vo.requestvo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

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
    private Integer timeSectionId;
    @ApiModelProperty(value = "分时区间名称",required = true)
    @NotBlank(message = "分时区间名称不能为空")
    private String periods;
    @ApiModelProperty(value = "分时区间开始时间",required = true)
    @NotBlank(message = "分时区间开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "分时区间结束时间",required = true)
    @NotBlank(message = "分时区间结束不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
}
