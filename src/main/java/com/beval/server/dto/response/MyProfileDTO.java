package com.beval.server.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private LocalDateTime createdOn;
    private ImageDTO profileImage;
    private ImageDTO bannerImage;
    private Set<SubredditDTO> subreddits;
}
