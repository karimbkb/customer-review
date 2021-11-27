package com.karimbkb.customerreview.dto;

import com.karimbkb.customerreview.validator.ReviewStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReviewDescriptionDto {
    @Positive
    private long reviewId;

    @Positive
    private int customerId;

    @NotNull @NotEmpty
    private String title;

    @NotNull @NotEmpty
    private String description;

    @NotNull @NotEmpty
    private String firstName;

    @NotNull @NotEmpty
    private String lastName;

    @Positive
    @Min(1)
    @Max(10)
    private int rating;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    @ReviewStatus
    private String status;

    @Positive
    @NotNull
    @NotEmpty
    private int version;
}
