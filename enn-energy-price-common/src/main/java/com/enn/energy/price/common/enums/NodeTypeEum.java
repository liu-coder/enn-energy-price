package com.enn.energy.price.common.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NodeTypeEum {
    ENTERPRISSE("0", "企业"),
    NODE("1", "目录（根节点）"),
    DEVICE("2", "设备");
    String type;
    String name;

    public static NodeTypeEum findMsgByType(Integer type) {
        if (ObjectUtil.isNull(type)) {
            return null;
        }
        for (NodeTypeEum changeTypeEum : values()) {
            if (changeTypeEum.getType().equals(type)) {
                return changeTypeEum;
            }
        }
        return null;
    }
}
