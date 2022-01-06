package com.enn.energy.price.client.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2022/1/6 13:55
 * @description：快乐工作每一天
 */
@Data
public class ElectricityPriceVersionDetailListRespDTO implements Serializable {
    private List<ElectricityPriceVersionDetailRespDTO> versionDetailList;
}
