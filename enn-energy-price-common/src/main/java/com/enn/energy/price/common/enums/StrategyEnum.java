package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StrategyEnum {
    DOUBLESTRATEGY((byte) 1, "两部制"),
    SINGLESTRATEGY((byte) 0, "单一制"),
    ;

    private Byte code;
    private String desc;
}
