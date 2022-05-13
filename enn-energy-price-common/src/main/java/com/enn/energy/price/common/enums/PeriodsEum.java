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
    SHARP("0","尖"),
    PEAK("1","峰"),
    LEVEL("2","平"),
    VALLEY("3","谷")
    ;
    String code;
    String desc;

    public static String getDesc(String code){
        PeriodsEum periodsEum = Arrays.stream( values() ).filter( t -> t.getCode().equals(code) ).findFirst().orElse( null );
        if(null != periodsEum){
            return periodsEum.getDesc();
        }
        return null;
    }

}
