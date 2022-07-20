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
    private String profileImageUrl;
    private LocalDateTime createdOn;
}
