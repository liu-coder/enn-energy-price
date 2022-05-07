package com.enn.energy.price.web.validator;

/**
 * 校验格式常量
 *
 * @author wanglongab
 */
public interface ValidPattern {
    String DECIMAL = "^(([1-9]\\d{0,4})|(0))([.]\\d{0,10})?$";
    String DECIMAL_TEMPERATURE = "^([\\+ \\-]?(([1-9]\\d{0,1})|(0)))([.]\\d{0,2})?$";
}
