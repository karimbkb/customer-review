package com.karimbkb.customerreview.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDTO {

    @ApiModelProperty(value = "Product Id of the Review", example = "93ae5569-93c7-4e2d-8415-ad1f0254f635", dataType = "UUID")
    private UUID productId;

    @Positive
    @ApiModelProperty(value = "Store Id of the Review", example = "2", dataType = "Integer")
    private Integer storeId;
}
