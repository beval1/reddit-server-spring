package com.beval.server.service;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.model.entity.ImageEntity;

public interface CloudinaryService {
    ImageEntity upload(ImageUploadPayloadDTO imageUploadDTO);
    boolean delete(ImageEntity imageEntity);
}
