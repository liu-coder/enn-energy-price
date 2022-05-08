package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StrategyEnum {
    DOUBLESTRAGEY((byte) 1, "两部制"),
    SINGLESTRAGEY((byte) 0, "单一制"),
    ;

    private Byte code;
    private String desc;
}
