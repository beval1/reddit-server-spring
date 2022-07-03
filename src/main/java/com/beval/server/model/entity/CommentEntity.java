package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    private String content;

    @OneToOne
    private UserEntity author;

    @OneToOne
    private CommentEntity parentComment;

    @OneToOne
    private PostEntity post;

}
