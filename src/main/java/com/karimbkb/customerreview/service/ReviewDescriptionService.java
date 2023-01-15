package com.karimbkb.customerreview.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.ReviewDescription;
import com.karimbkb.customerreview.dto.ReviewDescriptionCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionDTO;
import com.karimbkb.customerreview.exceptions.InputValidationException;
import com.karimbkb.customerreview.repositories.ReviewDescriptionRepository;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewDescriptionService {
    private final ReviewDescriptionRepository reviewDescriptionRepository;
    private final MapperFacade mapperFacade;

    public ReviewDescriptionDTO createReviewDescription(ReviewDescriptionCreateDTO reviewDescriptionCreateDTO) {
        log.debug("Creating new review description with data [{}]", reviewDescriptionCreateDTO);
        try {
            return ofNullable(reviewDescriptionCreateDTO)
                    .map(dto -> mapperFacade.map(dto, ReviewDescription.class))
                    .map(reviewDescription -> {
                        if(reviewDescription.getStatus() == null) {
                            reviewDescription.setStatus(ReviewDescription.StatusEnum.pending);
                        }
                        return reviewDescription;
                    })
                    .map(reviewDescriptionRepository::save)
                    .map(reviewDescription -> mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class))
                    .orElseThrow(InputValidationException::new);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(format("A review description with the same attributes as [%s] already exists", reviewDescriptionCreateDTO));
        }
    }

    public void patchReviewDescription(UUID reviewDescriptionId, JsonPatch patch) throws NotFoundException {
        log.debug("Updating review description for id [{}] and data [{}]", reviewDescriptionId, patch);
        ReviewDescription reviewDescriptionPatched = reviewDescriptionRepository.findById(reviewDescriptionId)
                .map(reviewDescription -> {
                    ReviewDescription reviewDescriptionUpdate = PatchUtil.applyPatch(patch, reviewDescription, ReviewDescription.class);
                    reviewDescriptionRepository.save(reviewDescriptionUpdate);
                    log.debug("Successfully updated review [{}]", reviewDescriptionUpdate);
                    return reviewDescriptionUpdate;
                })
                .orElseThrow(() -> new NotFoundException(format("No review found with given id [%s]", reviewDescriptionId)));
        log.debug("Patch has Successfully applied [{}]", reviewDescriptionPatched);
    }

    public Page<ReviewDescriptionDTO> getAllReviewDescriptions(PageRequest pageRequest) {
        return reviewDescriptionRepository.findAll(pageRequest)
                .map(reviewDescription -> mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class));

    }

    public void deleteReviewDescription(@NonNull UUID id) {
        reviewDescriptionRepository.deleteById(id);
        log.debug("ReviewDescription with id [{}] was deleted", id);
    }

    public List<ReviewDescription> getAllReviewDescriptionsByReviewId(@NonNull UUID reviewId) {
        log.debug("Get all review descriptions by review id [{}]", reviewId);
        return reviewDescriptionRepository.findAllByReviewId(reviewId);
    }

    public void deleteAllReviewDescriptionsById(@NonNull Collection<UUID> reviewDescriptionIds) {
        log.debug("Delete all review descriptions with ids [{}]", reviewDescriptionIds);
        reviewDescriptionRepository.deleteAllByIdInBatch(reviewDescriptionIds);
    }

    public Optional<ReviewDescriptionDTO> loadReviewDescriptionById(UUID id) {
        return reviewDescriptionRepository.findById(id)
                .map(reviewDescription -> mapperFacade.map(reviewDescription, ReviewDescriptionDTO.class));
    }
}
