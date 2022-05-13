package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CompareEum {
    EQUALS(0,"等于"),
    LESS(1,"小于"),
    LARGER(2,"大于")
    ;
    int code;
    String desc;


    public String getDesc(int code){
        CompareEum compareEum = Arrays.stream( values() ).filter( t -> t.getCode()==code ).findFirst().orElse( null );
        if(null != compareEum){
            return compareEum.getDesc();
        }
        return null;
    }
}
