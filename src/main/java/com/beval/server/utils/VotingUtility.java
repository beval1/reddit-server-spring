package com.beval.server.utils;

import com.beval.server.dto.response.AbstractUpvotableDTO;
import com.beval.server.model.entity.UpvotableEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.security.UserPrincipal;

public interface VotingUtility {
    void vote(Long entityId, UserPrincipal userPrincipal, String action);

    void setUpvotedAndDownvotedForUser(UpvotableEntity upvotable, AbstractUpvotableDTO upvotableDTO, UserEntity user);

    void setVotes(UpvotableEntity upvotable, AbstractUpvotableDTO upvotableDTO);
}
