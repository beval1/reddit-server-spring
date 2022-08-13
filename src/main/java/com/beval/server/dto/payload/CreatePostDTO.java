package com.beval.server.dto.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDTO {
    private String title;
    @JsonProperty(value = "comment")
    private CreateCommentDTO originalComment;
    private ImageUploadPayloadDTO image;
    private String link;
}
