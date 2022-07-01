package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CommentEntity extends BaseEntity {
    private String content;

    @OneToOne
    private UserEntity author;

    @OneToOne
    private CommentEntity parentComment;

    @OneToOne
    private PostEntity post;

}
