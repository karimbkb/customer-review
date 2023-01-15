package com.karimbkb.customerreview.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.karimbkb.customerreview.dto.ReviewDescriptionCreateDTO;
import com.karimbkb.customerreview.dto.ReviewDescriptionDTO;
import com.karimbkb.customerreview.exceptions.ReviewDescriptionNotFoundException;
import com.karimbkb.customerreview.service.ReviewDescriptionService;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reviewDescription")
public class ReviewDescriptionController {
    private final ReviewDescriptionService reviewDescriptionService;
    private final MapperFacade mapperFacade;

    @GetMapping
    public ResponseEntity<Iterable<ReviewDescriptionDTO>> getReviewDescriptions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok(reviewDescriptionService.getAllReviewDescriptions(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDescriptionDTO> getReviewDescription(@PathVariable UUID id) {
        return reviewDescriptionService
                .loadReviewDescriptionById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ReviewDescriptionNotFoundException.withId(id));
    }

    @PostMapping
    public ResponseEntity<ReviewDescriptionDTO> createReviewDescription(@RequestBody ReviewDescriptionCreateDTO reviewDescriptionCreateDTO, UriComponentsBuilder uriBuilder) {
        ReviewDescriptionDTO reviewDescription = reviewDescriptionService.createReviewDescription(reviewDescriptionCreateDTO);
        URI location = uriBuilder.path("api/v1/reviewDescription/{id}").buildAndExpand(reviewDescription.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(reviewDescription);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete review description by given id")
    public void deleteReviewDescription(@PathVariable UUID id) {
        reviewDescriptionService.deleteReviewDescription(id);
    }

    @ResponseStatus(OK)
    @PatchMapping("/{id}")
    @ApiOperation(value = "Patch review description by given id")
    public void updateReviewDescription(@PathVariable UUID id, @Valid @RequestBody JsonPatch patch) throws NotFoundException {
        reviewDescriptionService.patchReviewDescription(id, patch);
    }
}
