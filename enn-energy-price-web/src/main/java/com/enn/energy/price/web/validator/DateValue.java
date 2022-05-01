package com.enn.energy.price.web.validator;

import cn.hutool.core.date.DatePattern;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * 日期格式校验注解
 *
 * @author wanglongab
 */
@Documented
@Constraint(validatedBy = {DateValueValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface DateValue {
    String message() default "date format invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] format() default {DatePattern.NORM_DATE_PATTERN};

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        DateValue[] value();
    }
}
