package com.beval.server.dto.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDTO {
    private String content;
}
