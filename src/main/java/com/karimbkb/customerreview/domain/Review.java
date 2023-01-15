package com.karimbkb.customerreview.domain;

import com.karimbkb.customerreview.service.VerifyVersionReview;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Version;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(VerifyVersionReview.class)
@Table(name = "review", schema = "customer_review")
@Entity(name = "review")
public class Review implements Serializable {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private Integer storeId;
    private UUID productId;

    @Version
    private Integer version;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "reviewId")
    private List<ReviewDescription> reviewDescriptions = new ArrayList<>();
}
