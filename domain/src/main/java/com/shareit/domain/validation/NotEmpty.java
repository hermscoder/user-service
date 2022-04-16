package com.shareit.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmpty {
    public String message() default "Missing param: %s";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
