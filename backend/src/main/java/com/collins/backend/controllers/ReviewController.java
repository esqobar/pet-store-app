package com.collins.backend.controllers;

import com.collins.backend.dtos.ReviewDto;
import com.collins.backend.entities.Review;
import com.collins.backend.exceptions.AlreadyExistsException;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.payloads.requests.ReviewUpdateRequest;
import com.collins.backend.payloads.responses.ApiResponse;
import com.collins.backend.services.reviews.ReviewService;
import com.collins.backend.utils.FeedBackMessages;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.collins.backend.utils.FeedBackMessages.*;
import static com.collins.backend.utils.UrlMappings.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(REVIEWS_API)
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping(SUBMIT_REVIEW)
     public ResponseEntity<ApiResponse> saveReview(@RequestBody Review review,
                                                  @RequestParam Long reviewerId,
                                                  @RequestParam Long vetId) {
        try {
            reviewService.saveReview(review, reviewerId, vetId);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Review saved successfully.", null));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(new ApiResponse(e.getMessage(), null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(REVIEWS_BY_USER)
    public ResponseEntity<ApiResponse> getReviewsByUserId(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, page, size);
        Page<ReviewDto> reviewDtos = reviewPage.map((element) -> modelMapper.map(element, ReviewDto.class));
        return ResponseEntity.status(FOUND).body(new ApiResponse(FOUND_SUCCESS, reviewDtos));
    }

    @PutMapping(UPDATE_REVIEW)
    public ResponseEntity<ApiResponse> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest request) {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, request);
            return ResponseEntity.status(OK).body(new ApiResponse(UPDATE_SUCCESS, updatedReview.getId()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping(DELETE_REVIEW)
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.status(OK).body(new ApiResponse(DELETE_SUCCESS, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(AVG_RATING_FOR_VET)
    public ResponseEntity<ApiResponse> getAverageRatingForVet(@PathVariable Long vetId){
        double averageRating = reviewService.getAvgRatingForVet(vetId);
        return ResponseEntity.ok(new ApiResponse(FOUND_SUCCESS, averageRating));
    }
}
