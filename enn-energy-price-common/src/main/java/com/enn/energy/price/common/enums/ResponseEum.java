package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseEum {
    VERSION_UPDATE_FAIL("-1","版本更新失败"),
    VERSION_STRUCTURE_NAME_DUPLICATE("-1","版本下存在重复的体系名称"),
    EXIST_STRUCTURE_EQUIPMENT("-1","删除的体系中存在已绑定的设备的体系,请核对后再试"),
    UPDATE_STRUCTURE_AREA_NO_COVER("-1","修改的体系中存在已绑定设备的体系且区域未覆盖体系所包含的区域,请核对后再试");
    private String code;
    private String msg;


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
