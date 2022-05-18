package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author:quyl
 * @createTime:2022/5/17 8:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ElectricityPriceBindEditBO implements Serializable {

    private Long id;

    private String nextChangeFlag;

    private Long nextId;

}