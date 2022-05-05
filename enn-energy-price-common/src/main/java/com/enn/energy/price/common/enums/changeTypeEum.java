package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum changeTypeEum {
    ADD(0,"add"),
    DELETE(1,"delete"),
    UPDATE(2,"update");
    Integer type;
    String msg;
}
