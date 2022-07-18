package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO extends AbstractUpvotableDTO {
    private Long id;
    private String title;
}
