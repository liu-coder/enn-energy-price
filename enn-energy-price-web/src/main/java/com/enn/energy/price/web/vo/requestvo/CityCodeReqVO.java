package com.enn.energy.price.web.vo.requestvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 9:35
 */
@ApiModel("城市编码请求vo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityCodeReqVO {
    @ApiModelProperty(value = "省编码",required = false)
    private String provinceCode;

    @ApiModelProperty(value = "省名称",required = false)
    private String province;

}
