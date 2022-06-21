package com.beval.reddit_server_spring.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment extends BaseEntity {
    private String content;
    @OneToOne
    private User author;
    @OneToOne
    private Comment parentComment;

    @OneToOne
    private Post post;

}
