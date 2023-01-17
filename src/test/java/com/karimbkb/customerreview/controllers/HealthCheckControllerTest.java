package com.karimbkb.customerreview.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HealthCheckControllerTest {

    HealthCheckController controller = new HealthCheckController();

    @Test
    void shouldReturnOk() {
        assertThat(controller.getHealth()).isEqualTo(ResponseEntity.ok().build());
    }
}