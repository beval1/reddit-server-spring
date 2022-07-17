package com.beval.server.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private AuthorDTO author;
    private List<CommentDTO> replies;
    private int repliesCount;
    private int upVotes;
    private int downVotes;
}
