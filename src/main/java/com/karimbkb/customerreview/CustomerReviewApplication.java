package com.karimbkb.customerreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
//@EnableDiscoveryClient
public class CustomerReviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerReviewApplication.class, args);
    }
}

