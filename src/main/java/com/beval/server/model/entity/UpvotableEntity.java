package com.beval.server.model.entity;

import com.beval.server.model.interfaces.Upvotable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
    private boolean archived = false;

    @OneToOne
    @NotNull
    private UserEntity author;

    @Override
    public Set<UserEntity> getUpvotedUsers() {
        return upvotedUsers;
    }

    public void setUpvotedUsers(Set<UserEntity> upvotedUsers) {
        this.upvotedUsers = upvotedUsers;
    }

    @Override
    public Set<UserEntity> getDownvotedUsers() {
        return downvotedUsers;
    }

    public void setDownvotedUsers(Set<UserEntity> downvotedUsers) {
        this.downvotedUsers = downvotedUsers;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
