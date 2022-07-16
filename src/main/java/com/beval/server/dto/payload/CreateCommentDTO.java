package com.beval.server.dto.payload;

import com.beval.server.dto.response.AuthorDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDTO {
    private String content;
}
