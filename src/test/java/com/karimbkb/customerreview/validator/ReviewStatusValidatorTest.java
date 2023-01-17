package com.karimbkb.customerreview.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReviewStatusValidatorTest {

    @Mock
    ConstraintValidatorContext context;

    ReviewStatusValidator reviewStatusValidator = new ReviewStatusValidator();

    @Test
    void shouldReturnApprovedStatusIsValid() {
        assertThat(reviewStatusValidator.isValid("approved", context)).isTrue();
    }

    @Test
    void shouldReturnPendingStatusIsValid() {
        assertThat(reviewStatusValidator.isValid("pending", context)).isTrue();
    }

    @Test
    void shouldReturnRejectedStatusIsValid() {
        assertThat(reviewStatusValidator.isValid("rejected", context)).isTrue();
    }

    @Test
    void shouldReturnStatusIsNotValid() {
        assertThat(reviewStatusValidator.isValid("overdue", context)).isFalse();
    }
}