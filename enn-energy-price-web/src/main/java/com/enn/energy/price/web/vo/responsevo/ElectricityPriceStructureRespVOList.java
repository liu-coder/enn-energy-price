package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElectricityPriceStructureRespVOList implements Serializable {
    List<ElectricityPriceStructureRespVO> electricityPriceStructureRespVOList;
}
