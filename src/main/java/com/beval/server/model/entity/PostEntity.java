package com.beval.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
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
