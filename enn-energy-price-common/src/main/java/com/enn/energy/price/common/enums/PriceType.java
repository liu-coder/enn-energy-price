package com.enn.energy.price.common.enums;

import lombok.AllArgsConstructor;

/**
 * 电价类型
 * @author xinao
 *
 */
@AllArgsConstructor
public enum PriceType {
	//自定义电价
	custom(1), 
	//目录电价
	catalogue(2),
	
	//单一电价(自定义电价)
	meteringCustom(3);
	
	private final int priceType;

	public static PriceType valueOf(int type) {
		for (PriceType priceType : values()) {
            if (priceType.priceType == type) {
                return priceType;
            }
        }
		return null;
	}
}
