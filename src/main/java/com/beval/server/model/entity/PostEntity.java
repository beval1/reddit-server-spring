package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.beval.server.config.AppConstants.MAXIMUM_TITLE_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_TITLE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity {
    @NotNull
    @Length(max = MAXIMUM_TITLE_LENGTH, min = MINIMUM_TITLE_LENGTH)
    private String title;

    @NotNull
    @OneToOne
    private UserEntity author;

    @NotNull
    @ManyToOne
    private SubredditEntity subreddit;

}
