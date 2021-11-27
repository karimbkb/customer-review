package com.karimbkb.customerreview.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karimbkb.customerreview.service.VerifyVersionReviewDescription;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EntityListeners(VerifyVersionReviewDescription.class)
@Entity(name = "review_description")
public class ReviewDescription {
    public enum StatusEnum {approved, pending, rejected};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewDescriptionId;

    private long reviewId;
    private int customerId;
    private String title;
    private String description;
    private String firstName;
    private String lastName;
    private int rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Version
    private int version;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="reviewId", insertable = false, updatable = false)
    private Review review;
}
