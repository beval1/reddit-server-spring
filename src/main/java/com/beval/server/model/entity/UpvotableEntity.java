package com.beval.server.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
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
    @JoinTable(name="user_upvotable_upvotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "upvotable_id"))
    @Builder.Default
    private Set<UserEntity> upvotedUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(name="user_upvotable_downvotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "downvotable_id"))
    @Builder.Default
    private Set<UserEntity> downvotedUsers = new HashSet<>();

    @NotNull
    @Builder.Default
    private boolean archived = false;

    @OneToOne
    @NotNull
    private UserEntity author;

}
