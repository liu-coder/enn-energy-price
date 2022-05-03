package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BoolLogic {
    YES((byte) 1, "逻辑是"),
    NO((byte) 0, "逻辑否"),
    ;

    private Byte code;
    private String desc;
}
