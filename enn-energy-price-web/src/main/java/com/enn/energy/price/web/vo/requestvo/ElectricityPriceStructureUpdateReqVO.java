package com.enn.energy.price.web.vo.requestvo;

import com.enn.energy.price.biz.service.bo.proxyprice.ValidationList;
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
 * @Date 2022/5/1 10:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("电价价格体系修改vo")
public class ElectricityPriceStructureUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 804237214764593129L;
    @ApiModelProperty(value = "体系id,新增的体系不传id,修改的体系需要传")
    private String id;
    @ApiModelProperty(value = "体系名称",required = true)
    @NotBlank(message = "体系名称不能为空")
    @Length(min = 1, max = 50, message = "体系名称最大长度50")
    private String structureName;

    @ApiModelProperty(value = "省编码",required = true)
    private String provinceCode;

    @ApiModelProperty(value = "市编码,多个用,进行拼接",required = true)
    private String cityCodes;

    @ApiModelProperty(value = "区县编码不能为空,多个用,拼接",required = true)
    private String districtCodes;

    @ApiModelProperty(value = "变更类型,2:修改")
    private Integer changeType;


    @ApiModelProperty(value = "父体系id", required = true, dataType = "string")
    private String parentid;

    @ApiModelProperty(value = "电价价格列表",required = true)
    @NotEmpty(message = "电价价格列表不能为空")
    private List<@Valid ElectricityPriceUpdateReqVO> electricityPriceUpdateReqVOList;

    @ApiModelProperty(value = "电价季节分时列表",required = true)
    @NotEmpty(message = "电价季节分时列表不能为空")
    private List<@Valid ElectricityPriceSeasonRuleUpdateReqVO> electricityPriceSeasonRuleUpdateReqVOList;
}
