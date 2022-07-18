package com.beval.server.dto.interfaces;

public interface UpvotableDTO {
    void setVotes(int votes);
    int getVotes();
    void setUpvotedByUser(boolean isUpvoted);
    void setDownvotedByUser(boolean isdownvoted);
    boolean isUpvotedByUser();
    boolean isDownvotedByUser();
}
