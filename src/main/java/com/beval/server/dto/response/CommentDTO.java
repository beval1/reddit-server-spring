package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO author;
    private CommentDTO parentComment;
}
