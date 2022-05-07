package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StateEum {
    NORMAL(0,"正常"),
    DELETE(1,"删除");
    private int value;
    private String description;



}
