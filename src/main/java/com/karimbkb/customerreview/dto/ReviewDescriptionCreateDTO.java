package com.karimbkb.customerreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDescriptionCreateDTO {
    @NotNull
    private UUID reviewId;

    @NotNull
    private UUID customerId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Positive
    @Min(1)
    @Max(10)
    private Integer rating;
}
