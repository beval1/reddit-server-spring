package com.beval.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "comments")
public class CommentEntity extends UpvotableEntity {
    @NotNull
    @Length(max = 255)
    private String content;

    @OneToOne
    private CommentEntity parentComment;

    @OneToOne
    @NotNull
    private PostEntity post;

}
