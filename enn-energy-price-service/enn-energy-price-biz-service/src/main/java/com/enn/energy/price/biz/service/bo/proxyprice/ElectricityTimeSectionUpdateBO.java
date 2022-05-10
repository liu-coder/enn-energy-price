package com.enn.energy.price.biz.service.bo.proxyprice;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Date 2022/5/1 17:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityTimeSectionUpdateBO implements Serializable {
    private static final long serialVersionUID = -6215969465976610323L;
    private Integer timeSectionId;
    private String periods;
    private String startTime;
    private String endTime;
    private Boolean comply;
}
