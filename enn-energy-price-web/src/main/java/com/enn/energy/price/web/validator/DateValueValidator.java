package com.enn.energy.price.web.validator;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

/**
 * 日期格式校验器实现
 *
 * @author wanglongab
 */
public class DateValueValidator implements ConstraintValidator<DateValue, String> {
    private String[] formats;

    @Override
    public void initialize(DateValue constraintAnnotation) {
        formats = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value) || StrUtil.isBlank(value.trim())) {
            return true;
        }
        if (ArrayUtil.isEmpty(formats)) {
            return true;
        }
        for (String string : formats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(string);
                dateFormat.parse(value);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
