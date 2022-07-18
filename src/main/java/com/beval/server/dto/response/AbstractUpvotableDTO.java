package com.beval.server.dto.response;

import com.beval.server.dto.interfaces.UpvotableDTO;

public abstract class AbstractUpvotableDTO implements UpvotableDTO {
    private int votes;
    private boolean upvotedByUser;
    private boolean downvotedByUser;

    @Override
    public int getVotes() {
        return votes;
    }

    @Override
    public boolean isUpvotedByUser() {
        return upvotedByUser;
    }

    @Override
    public boolean isDownvotedByUser() {
        return downvotedByUser;
    }

    @Override
    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public void setUpvotedByUser(boolean upvotedByUser) {
        this.upvotedByUser = upvotedByUser;
    }

    @Override
    public void setDownvotedByUser(boolean downvotedByUser) {
        this.downvotedByUser = downvotedByUser;
    }
}
