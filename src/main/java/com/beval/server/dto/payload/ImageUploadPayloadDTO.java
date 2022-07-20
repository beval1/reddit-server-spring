package com.beval.server.dto.payload;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUploadPayloadDTO {
    private MultipartFile multipartFile;
    private String title;
}
