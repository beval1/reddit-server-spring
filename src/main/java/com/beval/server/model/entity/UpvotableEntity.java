package com.beval.server.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public abstract class UpvotableEntity extends BaseEntity {
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

    @ManyToOne
    @NotNull
    private SubredditEntity subreddit;

}
