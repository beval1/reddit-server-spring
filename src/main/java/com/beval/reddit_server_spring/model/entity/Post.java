package com.beval.reddit_server_spring.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseEntity{
    private String title;

    @OneToOne(fetch = FetchType.EAGER)
    private User author;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;

//    @ManyToOne
//    @JoinColumn(name = "subreddit_id")
//    private Subreddit subreddit;

}
