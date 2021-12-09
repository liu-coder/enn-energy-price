package com.enn.energy.price.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：chenchangtong
 * @date ：Created 2021/11/23 9:05
 * @description：快乐工作每一天
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPriceDictionarySelectDTO {
    private Long id;
    private String code;
    private String name;
    private Integer type;
    private String typeDesc;
    private Integer state;
}
