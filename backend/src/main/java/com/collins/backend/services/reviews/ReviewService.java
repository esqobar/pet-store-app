package com.collins.backend.services.reviews;

import com.collins.backend.entities.Review;
import com.collins.backend.entities.User;
import com.collins.backend.entities.enums.AppointmentStatus;
import com.collins.backend.exceptions.AlreadyExistsException;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.payloads.requests.ReviewUpdateRequest;
import com.collins.backend.repositories.AppointmentRepository;
import com.collins.backend.repositories.ReviewRepository;
import com.collins.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.collins.backend.utils.FeedBackMessages.*;

@Service
@RequiredArgsConstructor
public class ReviewService implements ReviewServiceImpl{
    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public Review saveReview(Review review, Long reviewerId, Long veterinarianId) {
        //1. check if the reviewer is the same as the doc being reviewed
        if (veterinarianId.equals(reviewerId)) {
            throw new IllegalArgumentException(FEEDBACK_CANNOT_SUBMIT_REVIEW_SELF);
        }
        //2. check if the reviewer has previously submitted a review for this doc
        Optional<Review> existingReview = reviewRepository.findByVeterinarianIdAndPatientId(veterinarianId, reviewerId);
        if (existingReview.isPresent()) {
            throw new AlreadyExistsException(ALREADY_SUBMITTED_REVIEW);
        }
        //3. check if the reviewer has gotten a completed appointment with this doc
        boolean hadCompletedAppointments = appointmentRepository.existsByVeterinarianIdAndPatientIdAndStatus(veterinarianId, reviewerId, AppointmentStatus.COMPLETED);
        if (hadCompletedAppointments) {
            throw new IllegalStateException(NOT_ALLOWED_TO_SUBMIT_REVIEW);
        }
        //4. get the reviewer(patients) from the db
        User user = userRepository.findById(reviewerId).orElseThrow(() -> new ResourceNotFoundException(VET_OR_PATIENT_NOT_FOUND));
        //5. get the veterinarian from the db
        User vet = userRepository.findById(veterinarianId).orElseThrow(() -> new ResourceNotFoundException(VET_OR_PATIENT_NOT_FOUND));
        //5. set both to the review
        review.setPatient(user);
        review.setVeterinarian(vet);
        //7. set the review to the db
        reviewRepository.save(review);
        //6. save to the review
        return reviewRepository.save(review);
    }

    @Transactional
    @Override
    public double getAvgRatingForVet(Long veterinarianId) {
        List<Review> reviews = reviewRepository.findByVeterinarianId(veterinarianId);
        return reviews.isEmpty() ? 0 : reviews.stream()
                .mapToInt(Review :: getStars)
                .average()
                .orElse(0.0);
    }

    @Override
    public Review updateReview(Long reviewerId, ReviewUpdateRequest request) {
        return reviewRepository.findById(reviewerId)
                .map(existingReview -> {
                    existingReview.setStars(request.getStars());
                    existingReview.setFeedback(request.getFeedback());
                    return reviewRepository.save(existingReview);
                })
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<Review> findAllReviewsByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByUserId(userId, pageRequest);
    }

    @Override
    public void deleteReview(Long reviewerId) {
        reviewRepository.findById(reviewerId)
                .ifPresentOrElse(Review::removeRelationShip, ()->{
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
                });
        reviewRepository.deleteById(reviewerId);
    }
}
