package com.beval.server.dto.payload;
;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import static com.beval.server.config.AppConstants.MAXIMUM_COMMENT_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDTO {
    @NotNull
    @Length(max = MAXIMUM_COMMENT_LENGTH)
    private String content;
}
