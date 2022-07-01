package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostEntity extends BaseEntity {
    private String title;

    @OneToOne
    private UserEntity author;

    @OneToMany
    private List<CommentEntity> comments;

//    @ManyToOne
//    @JoinColumn(name = "subreddit_id")
//    private Subreddit subreddit;

}
