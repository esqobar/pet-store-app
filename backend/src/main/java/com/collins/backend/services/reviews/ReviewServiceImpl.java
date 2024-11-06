package com.collins.backend.services.reviews;

import com.collins.backend.entities.Review;
import com.collins.backend.payloads.requests.ReviewUpdateRequest;
import org.springframework.data.domain.Page;

public interface ReviewServiceImpl {
    Review saveReview(Review review, Long reviewerId, Long veterinarianId);
    double getAvgRatingForVet(Long veterinarianId);
    Review updateReview(Long reviewerId, ReviewUpdateRequest request);
    Page<Review> findAllReviewsByUserId(Long veterinarianId, int page, int size);

    void deleteReview(Long reviewerId);
}
