package com.karimbkb.customerreview.config;

import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionCreateDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class OrikaMapping extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(Review.class, ReviewCreateDTO.class).byDefault().register();
        factory.classMap(ReviewDescription.class, ReviewDescriptionCreateDTO.class).byDefault().register();
    }
}
