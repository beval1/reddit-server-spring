package com.beval.server.utils.validators;

import com.beval.server.utils.validators.impl.SubredditNameValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SubredditNameValidatorImpl.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SubredditNameValidator {
    String message() default "Invalid subreddit name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
