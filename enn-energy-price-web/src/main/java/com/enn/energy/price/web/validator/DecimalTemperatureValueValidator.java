package com.enn.energy.price.web.validator;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 货币金额格式校验器实现
 *
 * @author wanglongab
 */
public class DecimalTemperatureValueValidator implements ConstraintValidator<DecimalValue, String> {
    @Override
    public void initialize(DecimalValue decimalValue) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value) || StrUtil.isBlank(value.trim())) {
            return true;
        }
        try {
            Pattern pattern = Pattern.compile(ValidPattern.DECIMAL_TEMPERATURE);
            if (pattern.matcher(value).matches()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
