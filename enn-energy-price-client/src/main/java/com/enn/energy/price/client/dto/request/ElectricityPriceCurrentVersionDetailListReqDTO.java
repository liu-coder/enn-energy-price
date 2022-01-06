package com.enn.energy.price.client.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ：chenchangtong
 * @date ：Created 2022/1/5 10:14
 * @description：快乐工作每一天
 */
@Data
public class ElectricityPriceCurrentVersionDetailListReqDTO {
    @NotNull
    private List<ElectricityPriceCurrentVersionDetailReqDTO> versionDetailListReq;
}
