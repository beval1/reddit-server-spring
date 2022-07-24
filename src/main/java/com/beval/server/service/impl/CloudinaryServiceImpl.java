package com.beval.server.service.impl;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.exception.CloudinaryException;
import com.beval.server.model.entity.ImageEntity;
import com.beval.server.repository.ImageRepository;
import com.beval.server.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private static final String TEMP_FILE = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    public CloudinaryServiceImpl(Cloudinary cloudinary, ImageRepository imageRepository) {
        this.cloudinary = cloudinary;
        this.imageRepository = imageRepository;
    }

    @Override
    public ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO){
        return upload(imageUploadDTO, null);
    }
    @Override
    public ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO, String folderName) {

        File tempFile = null;
        try {
            tempFile = File.createTempFile(TEMP_FILE, imageUploadDTO.getMultipartFile().getOriginalFilename());
            imageUploadDTO.getMultipartFile().transferTo(tempFile);

            Map<String, String> options = null;
            if (folderName != null){
                options = Map.of(
                        "folder", folderName
                );
            }

            @SuppressWarnings("unchecked")
            Map<String, String> uploadResult = cloudinary.
                    uploader().
                    upload(tempFile, options);

            String url = uploadResult.getOrDefault(URL,
                    "https://i.pinimg.com/originals/c5/21/64/c52164749f7460c1ededf8992cd9a6ec.jpg");
            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            return imageRepository.save(ImageEntity
                    .builder()
                    .publicId(publicId)
                    .url(url)
                    .title(imageUploadDTO.getTitle())
                    .build());
        } catch (IOException e) {
            throw new CloudinaryException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cloudinary error: %s", e.getMessage()));
        } finally {
            if (tempFile != null) {
                cleanUp(tempFile.toPath());
            }
        }
    }

    @Override
    public boolean delete(ImageEntity imageEntity) {
        if (imageEntity != null) {
            try {
                //delete in cloudinary
                this.cloudinary.uploader().destroy(imageEntity.getPublicId(), Map.of());
                //delete ind database
                imageRepository.deleteById(imageEntity.getId());
            } catch (IOException e) {
                throw new CloudinaryException(HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format("Cloudinary error: %s", e.getMessage()));
            }
        }
        return false;
    }

    private void cleanUp(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new CloudinaryException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Cloudinary error: %s", e.getMessage()));
        }
    }
}
