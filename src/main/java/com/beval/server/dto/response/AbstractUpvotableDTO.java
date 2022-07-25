package com.beval.server.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractUpvotableDTO {
    private int votes;
    private boolean upvotedByUser;
    private boolean downvotedByUser;
}
