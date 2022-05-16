package com.enn.energy.price.common.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChangeTypeEum {
    ADD(0,"add"),
    DELETE(1,"delete"),
    UPDATE(2,"update");
    Integer type;
    String msg;

    public static ChangeTypeEum findMsgByType(Integer type) {
        if (ObjectUtil.isNull(type)) {
            return null;
        }
        for (ChangeTypeEum changeTypeEum : values()) {
            if (changeTypeEum.getType().equals(type)) {
                return changeTypeEum;
            }
        }
        return null;
    }
}
