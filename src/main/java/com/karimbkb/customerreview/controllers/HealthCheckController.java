package com.karimbkb.customerreview.controllers;

import com.karimbkb.customerreview.models.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthCheckController {

  @GetMapping
  public ResponseEntity<Iterable<Review>> getHealth() {
    return ResponseEntity.ok().build();
  }
}
