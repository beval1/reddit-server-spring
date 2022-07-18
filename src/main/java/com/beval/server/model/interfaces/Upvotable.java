package com.beval.server.model.interfaces;

import com.beval.server.model.entity.UserEntity;

import java.util.Set;

public interface Upvotable {
    Set<UserEntity> getUpvotedUsers();
    Set<UserEntity> getDownvotedUsers();
    boolean isArchived();
}
