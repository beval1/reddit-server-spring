package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDTO {
    private Long id;
    private String title;
    private String description;
}
