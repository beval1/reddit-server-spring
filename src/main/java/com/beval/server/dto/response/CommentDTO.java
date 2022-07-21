package com.beval.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO extends AbstractUpvotableDTO {
    private Long id;
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private AuthorDTO author;
    private List<CommentDTO> replies;
    private int repliesCount;

}
