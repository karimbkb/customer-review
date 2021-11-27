package com.karimbkb.customerreview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class ReviewDto {
    private long reviewId;

    @Positive
    private int productId;

    @Positive
    private int storeId;

    @Positive
    @NotNull
    @NotEmpty
    private int version;
}
