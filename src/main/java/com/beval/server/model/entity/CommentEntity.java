package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    @NotNull
    @Length(max = 255)
    private String content;

    @OneToOne
    @NotNull
    private UserEntity author;

    @OneToOne
    private CommentEntity parentComment;

    @OneToOne
    @NotNull
    private PostEntity post;

    @Builder.Default
    private int upVotes = 0;
    @Builder.Default
    private int downVotes = 0;

//    @Builder.Default
//    private boolean deleted = false;
}
