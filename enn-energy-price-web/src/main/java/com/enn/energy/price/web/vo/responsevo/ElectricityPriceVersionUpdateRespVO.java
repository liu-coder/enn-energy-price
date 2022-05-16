package com.enn.energy.price.web.vo.responsevo;

import cn.hutool.core.date.DatePattern;
import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
import com.enn.energy.price.web.validator.DateValue;
import com.enn.energy.price.web.vo.requestvo.ElectricityPriceStructureUpdateReqVO;
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

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/1 10:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("电价版本修改vo")
public class ElectricityPriceVersionUpdateRespVO implements Serializable {
    private static final long serialVersionUID = 211362895339802678L;
    @ApiModelProperty(value = "父版本id", required = true, dataType = "string")
    private String lastVersionId;
    @ApiModelProperty(value = "电价体系列表",required = true)
    @NotEmpty(message = "电价体系不能为空")
    @Valid
    private ValidationList<ElectricityPriceStructureUpdateRespVO> versionStructureRespVOList;

}
