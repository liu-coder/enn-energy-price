package com.enn.energy.price.common.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BindStatusEum {
    UNBOUND("0", "未绑定"),
    BIND("1", "已绑定"),
    INVALID("2", "已失效");
    String status;
    String name;

    public static BindStatusEum findMsgByType(Integer status) {
        if (ObjectUtil.isNull(status)) {
            return null;
        }
        for (BindStatusEum changeTypeEum : values()) {
            if (changeTypeEum.getStatus().equals(status)) {
                return changeTypeEum;
            }
        }
        return null;
    }
}
