package com.karimbkb.customerreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerReviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerReviewApplication.class, args);
    }
}
