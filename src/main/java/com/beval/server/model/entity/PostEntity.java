package com.beval.server.model.entity;

import com.beval.server.model.interfaces.Upvotable;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static com.beval.server.config.AppConstants.MAXIMUM_TITLE_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_TITLE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity implements Upvotable {
    @NotNull
    @Length(max = MAXIMUM_TITLE_LENGTH, min = MINIMUM_TITLE_LENGTH)
    private String title;

    @NotNull
    @OneToOne
    private UserEntity author;

    @NotNull
    @ManyToOne
    private SubredditEntity subreddit;

    @ManyToMany
    @Builder.Default
    private Set<UserEntity> upvotedUsers = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private Set<UserEntity> downvotedUsers = new HashSet<>();
}
