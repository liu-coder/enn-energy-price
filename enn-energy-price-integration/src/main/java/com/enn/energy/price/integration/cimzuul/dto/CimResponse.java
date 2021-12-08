package com.enn.energy.price.integration.cimzuul.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.rdfa.framework.biz.dto.BaseResponse;

/**
 * @author yangmugui
 * @Description: 通过设备类型查询cim封装参数
 * @date 2021-8-4
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CimResponse<T> extends BaseResponse {

    private String msg;

    private Integer code;

    private T data;

    public boolean success() {
        return code == 200;
    }

    @Override
    public String toLog() {
        return toString();
    }
}
