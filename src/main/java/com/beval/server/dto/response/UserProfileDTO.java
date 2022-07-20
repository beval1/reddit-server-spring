package com.beval.server.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String username;
    private ImageDTO profileImage;
    private ImageDTO bannerImage;
    private LocalDateTime createdOn;
}
