package com.beval.server.model.entity;

import com.beval.server.model.interfaces.Upvotable;
import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public abstract class UpvotableEntity extends BaseEntity implements Upvotable {
    @ManyToMany
    @Builder.Default
    private Set<UserEntity> upvotedUsers = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private Set<UserEntity> downvotedUsers = new HashSet<>();

    @NotNull
    @Builder.Default
    private boolean archived;

    @Override
    public Set<UserEntity> getUpvotedUsers() {
        return upvotedUsers;
    }

    @Override
    public Set<UserEntity> getDownvotedUsers() {
        return downvotedUsers;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }
}
