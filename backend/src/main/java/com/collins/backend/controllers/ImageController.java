package com.collins.backend.controllers;

import com.collins.backend.entities.Photo;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.payloads.responses.ApiResponse;
import com.collins.backend.services.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

import static com.collins.backend.utils.FeedBackMessages.*;
import static com.collins.backend.utils.UrlMappings.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(PHOTOS_API)
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(PHOTO_UPLOAD)
    public ResponseEntity<ApiResponse> savePhoto(
            @RequestParam MultipartFile file,
            @RequestParam Long userId) throws SQLException, IOException {
        try {
            Photo photo = imageService.saveImage(file, userId);
            return ResponseEntity.ok(new ApiResponse(UPLOAD_SUCCESS, photo.getId()));
        } catch (IOException | SQLException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping(PHOTO_BY_ID)
    public ResponseEntity<ApiResponse> getImageById(@PathVariable Long imageId) {
        try {
            Photo photo = imageService.getImageById(imageId);
            if (photo != null) {
                byte[] photoBytes = imageService.getImageData(photo.getId());
                return ResponseEntity.ok(new ApiResponse(FOUND_SUCCESS, photoBytes));
            }
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null, NOT_FOUND));
    }

    @DeleteMapping(DELETE_PHOTO)
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable Long imageId, @PathVariable Long userId) {
        try {
            Photo image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImage(image.getId(), userId);
                return ResponseEntity.ok(new ApiResponse(DELETE_SUCCESS, image.getId()));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null, INTERNAL_SERVER_ERROR));
    }


    @PutMapping(UPDATE_PHOTO)
    public ResponseEntity<ApiResponse> updatePhoto(@PathVariable Long imageId, @RequestBody MultipartFile file) throws SQLException {
        try {
            Photo image = imageService.getImageById(imageId);
            if (image != null) {
                Photo updatedPhoto = imageService.updateImage(image.getId(), file);
                return ResponseEntity.ok(new ApiResponse(UPDATE_SUCCESS, updatedPhoto.getId()));
            }
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(null, NOT_FOUND));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(null, INTERNAL_SERVER_ERROR));

    }
}
