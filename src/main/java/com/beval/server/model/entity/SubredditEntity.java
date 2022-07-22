package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.beval.server.config.AppConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subreddits")
public class SubredditEntity extends BaseEntity {
    @NotNull
    @Length(max = MAXIMUM_TITLE_LENGTH, min = MINIMUM_TITLE_LENGTH)
    @Column(unique = true)
    private String name;

    @NotNull
    @Length(max = MAXIMUM_SUBREDDIT_DESCRIPTION_LENGTH, min = MINIMUM_SUBREDDIT_DESCRIPTION_LENGTH)
    private String description;

    @NotNull
    @ManyToMany
    @JoinTable(name = "subbreddits_admins",
            joinColumns = @JoinColumn(name = "subbredit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> admins;

}
