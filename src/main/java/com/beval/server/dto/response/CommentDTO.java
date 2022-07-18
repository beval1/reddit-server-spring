package com.beval.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Long id;
    private String content;
    private AuthorDTO author;
    private List<CommentDTO> replies;
    private int repliesCount;
    private int votes;
    private boolean upVotedByUser;
    private boolean downVotedByUser;
}
