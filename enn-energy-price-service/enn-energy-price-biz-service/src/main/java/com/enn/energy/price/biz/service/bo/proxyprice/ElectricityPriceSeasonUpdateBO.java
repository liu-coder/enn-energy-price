package com.enn.energy.price.biz.service.bo.proxyprice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
 * @Date 2022/5/1 17:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceSeasonUpdateBO implements Serializable {
    private static final long serialVersionUID = 3323637832408269048L;
    private String seasonSectionId;
    private String seasonName;
    private List<SeansonDateBO> seasonDateList;
    private List<ElectricityPriceStrategyBO> electricityPriceStrategyBOList;

}
