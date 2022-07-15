package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne
    @NotNull
    private UserEntity author;

    @OneToMany
    private List<CommentEntity> comments;

//    @ManyToOne
//    @JoinColumn(name = "subreddit_id")
//    private Subreddit subreddit;

}
