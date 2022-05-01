package com.enn.energy.price.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * 货币金额格式校验注解
 *
 * @author wanglongab
 */
@Documented
@Constraint(validatedBy = {com.enn.billing.api.client.validator.DecimalValueValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface DecimalValue {
    String message() default "decimal format invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        DecimalValue[] value();
    }
}
