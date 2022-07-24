package com.beval.server.utils.validators.impl;

import com.beval.server.utils.validators.SubredditNameValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.beval.server.config.AppConstants.MAXIMUM_SUBREDDIT_NAME_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_SUBREDDIT_NAME_LENGTH;

public class SubredditNameValidatorImpl implements ConstraintValidator<SubredditNameValidator, String> {
    private String regex = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

    @Override
    public void initialize(SubredditNameValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() >= MINIMUM_SUBREDDIT_NAME_LENGTH && value.length() <= MAXIMUM_SUBREDDIT_NAME_LENGTH
                && value.matches(regex);
    }
}
