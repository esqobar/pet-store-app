package com.collins.backend.services.images;

import com.collins.backend.entities.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface ImageServiceImpl {
    Photo saveImage(MultipartFile file, Long userId) throws IOException, SQLException;
    Photo getImageById(Long id);
    void deleteImage(Long id, Long userId) throws SQLException;
    Photo updateImage(Long id, MultipartFile file) throws SQLException, IOException;
    byte[] getImageData(Long id) throws SQLException;
}
