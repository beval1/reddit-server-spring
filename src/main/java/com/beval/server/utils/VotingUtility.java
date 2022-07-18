package com.beval.server.utils;

import com.beval.server.dto.interfaces.UpvotableDTO;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.interfaces.Upvotable;
import com.beval.server.security.UserPrincipal;

public interface VotingUtility {
    void vote(String entityId, UserPrincipal userPrincipal, String action);

    void setUpvotedAndDownvotedForUser(Upvotable upvotable, UpvotableDTO upvotableDTO, UserEntity user);

    void setVotes(Upvotable upvotable, UpvotableDTO upvotableDTO);
}
