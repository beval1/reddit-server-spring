package com.beval.server.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO extends AbstractUpvotableDTO {
    private Long id;
    private String title;
    private String type;
    private String content;
    private AuthorDTO author;
    private SubredditDTO subreddit;
    private int commentsCount;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
