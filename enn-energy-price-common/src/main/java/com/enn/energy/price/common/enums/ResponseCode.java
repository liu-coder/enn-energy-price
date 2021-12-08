package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码枚举类.
 *
 * @author : wuchaon
 * @version : 1.0 2021/8/6 13:53
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("0", "success"),
    FAIL("-1", "fail");


    private String code;
    private String message;


}
