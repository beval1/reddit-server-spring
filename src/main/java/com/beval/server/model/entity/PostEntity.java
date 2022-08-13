package com.beval.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.beval.server.config.AppConstants.MAXIMUM_TITLE_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_TITLE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "posts")
public class PostEntity extends UpvotableEntity {
    @NotNull
    @Length(max = MAXIMUM_TITLE_LENGTH, min = MINIMUM_TITLE_LENGTH)
    private String title;

    @NotNull
    @ManyToOne
    private SubredditEntity subreddit;

    @NotNull
    private String type;
}
