package com.karimbkb.customerreview.models;

import com.karimbkb.customerreview.service.VerifyVersionReview;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Version;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EntityListeners(VerifyVersionReview.class)
@Entity(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    private int storeId;
    private int productId;

    @Version
    private int version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reviewId")
    @OrderBy("createdAt desc")
    private List<ReviewDescription> reviewDescriptions = new ArrayList<>();
}
