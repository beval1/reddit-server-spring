package com.beval.server.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDTO {
    private Long id;
    private String name;
    private String description;
    private String mainImageUrl;
    private String backgroundImageUrl;
    private int membersCount;
    private LocalDateTime createdOn;
}
