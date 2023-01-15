package com.karimbkb.customerreview.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDTO;
import com.karimbkb.customerreview.exceptions.InputValidationException;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import com.karimbkb.customerreview.util.PatchUtil;
import javassist.NotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewDescriptionService reviewDescriptionService;
    private final MapperFacade mapperFacade;

    public ReviewDTO createReview(ReviewCreateDTO reviewCreateDTO) {
        log.debug("Creating new review with data [{}]", reviewCreateDTO);
        try {
            return ofNullable(reviewCreateDTO)
                    .map(dto -> mapperFacade.map(dto, Review.class))
                    .map(reviewRepository::save)
                    .map(review -> mapperFacade.map(review, ReviewDTO.class))
                    .orElseThrow(InputValidationException::new);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(format("A review with the same attributes as [%s] already exists", reviewCreateDTO));
        }
    }

    public Optional<ReviewDTO> loadReviewById(@NonNull UUID id) {
        return reviewRepository.findById(id)
                .map(review -> mapperFacade.map(review, ReviewDTO.class));
    }

    public Page<ReviewDTO> getAllReviews(PageRequest pageRequest) {
        return reviewRepository.findAll(pageRequest)
                .map(review -> mapperFacade.map(review, ReviewDTO.class));
    }

    public void deleteReview(@NonNull UUID reviewId) {
        List<ReviewDescription> reviewDescriptions = reviewDescriptionService.getAllReviewDescriptionsByReviewId(reviewId);
        List<UUID> reviewDescriptionIds = reviewDescriptions.stream()
                .map(ReviewDescription::getId)
                .collect(Collectors.toList());

        reviewDescriptionService.deleteAllReviewDescriptionsById(reviewDescriptionIds);
        reviewRepository.deleteById(reviewId);

        log.debug("Review with id [{}] and review descriptions with ids [{}] were deleted", reviewId, reviewDescriptions);
    }

    public void patchReview(UUID reviewId, JsonPatch patch) throws NotFoundException {
        log.debug("Updating Review for id [{}] and data [{}]", reviewId, patch);
        Review reviewPatched = reviewRepository.findById(reviewId)
                .map(review -> {
                    Review reviewUpdate = PatchUtil.applyPatch(patch, review, Review.class);
                    reviewRepository.save(reviewUpdate);
                    log.debug("Successfully updated review [{}]", reviewUpdate);
                    return reviewUpdate;
                })
                .orElseThrow(() -> new NotFoundException(format("No review found with given id [%s]", reviewId)));
        log.debug("Patch has Successfully applied [{}]", reviewPatched);
    }
}
