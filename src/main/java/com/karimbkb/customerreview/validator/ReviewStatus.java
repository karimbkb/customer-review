package com.karimbkb.customerreview.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ReviewStatusValidator.class)
@Documented
public @interface ReviewStatus {

  String message() default "{ReviewStatus.invalid}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
