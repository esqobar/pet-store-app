package com.collins.backend.services.users;

import com.collins.backend.dtos.AppointmentDto;
import com.collins.backend.dtos.EntityConverter;
import com.collins.backend.dtos.ReviewDto;
import com.collins.backend.dtos.UserDto;
import com.collins.backend.entities.Review;
import com.collins.backend.entities.User;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.factories.UserFactory;
import com.collins.backend.payloads.requests.RegistrationRequest;
import com.collins.backend.payloads.requests.UserUpdateRequest;
import com.collins.backend.repositories.UserRepository;
import com.collins.backend.services.appointments.AppointmentService;
import com.collins.backend.services.images.ImageService;
import com.collins.backend.services.reviews.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.collins.backend.utils.FeedBackMessages.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final EntityConverter<User, UserDto> entityConverter;
    private final AppointmentService appointmentService;
    private final ImageService imageService;
    private final ReviewService reviewService;


    @Override
    public User register(RegistrationRequest request) {
        return userFactory.createUser(request);
    }

    @Override
    public User update(Long userId, UserUpdateRequest request) {
        User user = findById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setSpecialization(request.getSpecialization());
        return userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
    }

    @Override
    public void delete(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse( userRepository::delete, () -> {
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
                });
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> entityConverter.mapEntityToDto(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserWithDetails(Long userId) throws SQLException {
        //1. getting the user
        User user = findById(userId);

        //2. converting the user to a UserDto
        UserDto userDto = entityConverter.mapEntityToDto(user, UserDto.class);

        //3. getting the user appointments ( users (patients and vets ) )
        setUserAppointment(userDto);
        //.4 get users photo
        setUserPhoto(userDto, user);
        setUserReviews(userDto, userId);
        return  userDto;
    }

    private void setUserAppointment(UserDto userDto) {
        List<AppointmentDto> appointmentDtos = appointmentService.getUserAppointments(userDto.getId());
        userDto.setAppointments(appointmentDtos);
    }

    private void setUserPhoto(UserDto userDto, User user) throws SQLException {
        if (user.getPhoto() != null) {
            userDto.setPhotoId(user.getPhoto().getId());
            userDto.setPhoto(imageService.getImageData(user.getPhoto().getId()));
        }
    }

    private void setUserReviews(UserDto userDto, Long userId) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, 0, Integer.MAX_VALUE);
        List<ReviewDto> reviewDto = reviewPage.getContent()
                .stream()
                .map(this::mapReviewToDto).toList();
        if(!reviewDto.isEmpty()) {
            double averageRating = reviewService.getAvgRatingForVet(userId);
        }
        userDto.setReviews(reviewDto);
    }

    private ReviewDto mapReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setStars(review.getStars());
        reviewDto.setFeedback(review.getFeedback());
        mapVeterinarianInfo(reviewDto, review);
        mapPatientInfo(reviewDto, review);
        return reviewDto;
    }
    private void mapVeterinarianInfo(ReviewDto reviewDto, Review review){
        if (review.getVeterinarian() != null) {
            reviewDto.setVeterinarianId(review.getVeterinarian().getId());
            reviewDto.setVeterinarianName(review.getVeterinarian().getFirstName()+ " " + review.getVeterinarian().getLastName());
            // set the photo
            setVeterinarianPhoto(reviewDto, review);
        }
    }

    private void mapPatientInfo(ReviewDto reviewDto, Review review) {
        if (review.getPatient() != null) {
            reviewDto.setPatientId(review.getPatient().getId());
            reviewDto.setPatientName(review.getPatient().getFirstName()+ " " + review.getPatient().getLastName());
            // set the photo
            setReviewerPhoto(reviewDto, review);
        }
    }

    private void setReviewerPhoto(ReviewDto reviewDto, Review review) {
        if(review.getPatient().getPhoto() != null){
            try {
                reviewDto.setPatientImage(imageService.getImageData(review.getPatient().getPhoto().getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else {
            reviewDto.setPatientImage(null);
        }
    }

    private void setVeterinarianPhoto(ReviewDto reviewDto, Review review) {
        if (review.getVeterinarian().getPhoto() != null) {
            try {
                reviewDto.setVeterinarianImage(imageService.getImageData(review.getVeterinarian().getPhoto().getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            reviewDto.setVeterinarianImage(null);
        }
    }
}
