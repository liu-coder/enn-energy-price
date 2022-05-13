package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.CityBO;
import com.enn.energy.price.dal.po.mbg.CityCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("城市列表响应vo")
public class CityListRespVO implements Serializable {
    private static final long serialVersionUID = 4299558331737988604L;
    @ApiModelProperty("城市编码列表")
    List<CityRespVO> cityItemList;




}
