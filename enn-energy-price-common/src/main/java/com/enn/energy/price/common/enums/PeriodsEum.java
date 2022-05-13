package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 时间段枚举
 */
@Getter
@AllArgsConstructor
public enum PeriodsEum {
    SHARP(0,"尖"),
    PEAK(1,"峰"),
    LEVEL(2,"平"),
    VALLEY(3,"谷")
    ;
    int code;
    String desc;

    public String getDesc(int code){
        PeriodsEum periodsEum = Arrays.stream( values() ).filter( t -> t.getCode()==code ).findFirst().orElse( null );
        if(null != periodsEum){
            return periodsEum.getDesc();
        }
        return null;
    }

}
