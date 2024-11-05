package com.collins.backend.services.images;

import com.collins.backend.entities.Photo;
import com.collins.backend.entities.User;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.repositories.ImageRepository;
import com.collins.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import static com.collins.backend.utils.FeedBackMessages.*;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageServiceImpl {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Override
    public Photo saveImage(MultipartFile file, Long userId) throws SQLException, IOException {
        Optional<User> theUser = userRepository.findById(userId);
        Photo image = new Photo();
        if(file != null && !file.isEmpty()) {
            byte[] imageBytes = file.getBytes();
            Blob imageBlob = new SerialBlob(imageBytes);
            image.setPhoto(imageBlob);
            image.setFileType(file.getContentType());
        }
        Photo savedImage = imageRepository.save(image);
        theUser.ifPresent(user -> {
            user.setPhoto(savedImage);
        });
        userRepository.save(theUser.get());
        return savedImage;
    }

    @Override
    public Photo getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
    }

    @Transactional
    @Override
    public void deleteImage(Long id, Long userId) {
        userRepository.findById(userId).ifPresentOrElse(User::removeUserPhoto, () -> {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        });
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
                });
    }

    @Override
    public Photo updateImage(Long id, MultipartFile file) throws SQLException, IOException {
        Photo image = getImageById(id);
        if(image != null) {
            byte[] updatedImage = file.getBytes();
            Blob newImageBlob = new SerialBlob(updatedImage);
            image.setPhoto(newImageBlob);
            image.setFileType(file.getContentType());
            image.setFileName(file.getOriginalFilename());
            return imageRepository.save(image);
        }
        throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
    }

    @Override
    public byte[] getImageData(Long id) throws SQLException {
        Photo image = getImageById(id);
        if (image != null) {
            Blob photoBlob = image.getPhoto();
            int blobLength = (int) photoBlob.length();
            return new byte[blobLength];
        }
        return null;
    }
}