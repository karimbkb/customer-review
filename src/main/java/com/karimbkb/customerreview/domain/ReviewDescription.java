package com.karimbkb.customerreview.domain;

import com.karimbkb.customerreview.service.VerifyVersionReviewDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(VerifyVersionReviewDescription.class)
@Table(name = "review_description", schema = "customer_review")
@Entity(name = "review_description")
public class ReviewDescription implements Serializable {
    public enum StatusEnum {approved, pending, rejected};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private UUID reviewId;
    private UUID customerId;
    private String title;
    private String description;
    private String firstName;
    private String lastName;
    private Integer rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Version
    private Integer version;
}
