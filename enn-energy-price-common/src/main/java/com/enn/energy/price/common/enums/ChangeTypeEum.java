package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChangeTypeEum {
    UPDATE(2,"update");
    Integer type;
    String msg;
}
