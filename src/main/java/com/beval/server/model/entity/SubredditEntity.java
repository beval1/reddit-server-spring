package com.beval.server.model.entity;

import com.beval.server.utils.validators.SubredditNameValidator;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static com.beval.server.config.AppConstants.MAXIMUM_SUBREDDIT_DESCRIPTION_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_SUBREDDIT_DESCRIPTION_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subreddits")
public class SubredditEntity extends BaseEntity {
    @Column(unique = true)
    @SubredditNameValidator
    private String name;

    @NotNull
    @Length(max = MAXIMUM_SUBREDDIT_DESCRIPTION_LENGTH, min = MINIMUM_SUBREDDIT_DESCRIPTION_LENGTH)
    private String description;

    @OneToOne
    private ImageEntity mainImage;

    @OneToOne
    private ImageEntity backgroundImage;

    @NotNull
    @ManyToMany
    @JoinTable(name = "subbreddits_moderators",
            joinColumns = @JoinColumn(name = "subbredit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<UserEntity> moderators = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "subbreddits_bans",
            joinColumns = @JoinColumn(name = "subbredit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<UserEntity> bannedUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "users_subreddits",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subreddit_id"))
    @Builder.Default
    private Set<UserEntity> members = new HashSet<>();

}
