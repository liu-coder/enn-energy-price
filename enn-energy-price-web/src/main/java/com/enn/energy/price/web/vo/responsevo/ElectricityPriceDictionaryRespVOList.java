package com.enn.energy.price.web.vo.responsevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/8 16:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityPriceDictionaryRespVOList implements Serializable {
    List<ElectricityPriceDictionaryRespVO> electricityPriceDictionaryRespVOList;
}
