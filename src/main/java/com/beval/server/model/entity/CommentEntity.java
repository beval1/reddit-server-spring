package com.beval.server.model.entity;

import com.beval.server.model.interfaces.Upvotable;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity implements Upvotable {
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

    @ManyToMany
    @Builder.Default
    private Set<UserEntity> upvotedUsers = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private Set<UserEntity> downvotedUsers = new HashSet<>();

}
