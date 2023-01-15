package com.karimbkb.customerreview.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.domain.Review;
import com.karimbkb.customerreview.dto.ReviewDTO;
import com.karimbkb.customerreview.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewControllerPatchValidator implements Validator {

    private final ObjectMapper objectMapper;
    private final ReviewRepository reviewRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object object, Errors errors) {
        JsonPatch patch = (JsonPatch) object;
        JsonNode jsonPatchList = objectMapper.convertValue(patch, JsonNode.class);

        ReviewDTO reviewDTO = new ReviewDTO();
        for (int i = 0; i < jsonPatchList.size(); i++) {
            log.debug("Path: {}", jsonPatchList.get(i).get("path"));
            log.debug("Value: {}", jsonPatchList.get(i).get("value"));
            log.debug("Op: {}", jsonPatchList.get(i).get("op"));

            if (jsonPatchList.get(i).get("path").asText().equals("/version")) {
                reviewDTO.setVersion(getValue(jsonPatchList, i).asInt());
            }

            if (jsonPatchList.get(i).get("path").asText().equals("/storeId")) {
                reviewDTO.setStoreId(getValue(jsonPatchList, i).asInt());
            }

            if (jsonPatchList.get(i).get("path").asText().equals("/productId")) {
                reviewDTO.setProductId(UUID.fromString(getValue(jsonPatchList, i).asText()));
            }
        }

        if (reviewDTO.getVersion() == null) {
            errors.rejectValue("version", "version.parameter.required", "No version parameter was found in the request");
        }

        if (!isValidVersion(reviewDTO)) {
            errors.rejectValue("version", "invalid.version", "The provided version number is either smaller or equal the current resource version");
        }
    }

    private static JsonNode getValue(JsonNode jsonPatchList, int index) {
        return jsonPatchList.get(index).get("value");
    }

    public boolean isValidVersion(ReviewDTO reviewDTO) {
        Optional<Review> reviewResult = reviewRepository.findByStoreIdAndProductId(reviewDTO.getStoreId(), reviewDTO.getProductId());
        return reviewResult.isEmpty() || reviewResult.get().getVersion() < reviewDTO.getVersion();
    }
}
