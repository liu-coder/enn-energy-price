package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum StrategyEnum {
    DOUBLESTRATEGY((byte) 1, "两部制"),
    SINGLESTRATEGY((byte) 0, "单一制"),
    ;

    private Byte code;
    private String desc;


    public String getDesc(Byte code){
        StrategyEnum strategyEnum = Arrays.stream( values() ).filter( t -> t.getCode().equals( code ) ).findFirst().orElse( null );
        if(null != strategyEnum){
            return strategyEnum.getDesc();
        }
        return null;
    }
}
