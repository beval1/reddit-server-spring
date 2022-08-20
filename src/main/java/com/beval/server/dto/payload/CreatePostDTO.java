package com.beval.server.dto.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import static com.beval.server.config.AppConstants.MAXIMUM_TITLE_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_TITLE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDTO {
    @NotNull
    @Length(max = MAXIMUM_TITLE_LENGTH, min = MINIMUM_TITLE_LENGTH)
    private String title;
    @JsonProperty(value = "comment")
    private CreateCommentDTO originalComment;
    private ImageUploadPayloadDTO image;
    private String link;
}
