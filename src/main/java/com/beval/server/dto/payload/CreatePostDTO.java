package com.beval.server.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostDTO {
    private String title;
    private CreateCommentDTO originalComment;
}
