package com.karimbkb.customerreview.config;

import ma.glasnost.orika.MapperFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfiguration {

    @Bean
    public MapperFacade mapperFacade() {
        return new OrikaMapping();
    }
}
