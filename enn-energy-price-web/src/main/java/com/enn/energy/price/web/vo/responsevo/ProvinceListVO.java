package com.enn.energy.price.web.vo.responsevo;

import com.enn.energy.price.biz.service.bo.proxyprice.ProvinceBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/12 11:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceListVO implements Serializable {
    List<ProvinceVO> provinceVOList;
}
