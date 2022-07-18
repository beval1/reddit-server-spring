package com.beval.server.dto.response;

import com.beval.server.dto.interfaces.UpvotableDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO implements UpvotableDTO {
    private Long id;
    private String title;
    private int votes;
    private boolean upvotedByUser;
    private boolean downvotedByUser;
}
