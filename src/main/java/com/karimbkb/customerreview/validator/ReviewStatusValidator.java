package com.karimbkb.customerreview.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ReviewStatusValidator implements ConstraintValidator<ReviewStatus, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    Pattern pattern = Pattern.compile("^(pending|approved|rejected)$");
    Matcher matcher = pattern.matcher(value);
    try {
      return matcher.matches();
    } catch (Exception e) {
      return false;
    }
  }
}
