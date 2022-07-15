package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.beval.server.config.AppConstants.MAXIMUM_TITLE_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_TITLE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subreddits")
public class SubredditEntity extends BaseEntity{
    @NotNull
    @Length(max = MAXIMUM_TITLE_LENGTH, min = MINIMUM_TITLE_LENGTH)
    private String title;

//    @OneToMany
//    private List<PostEntity> posts;

    @NotNull
    @ManyToMany
    @JoinTable(name = "subbreddits_admins",
            joinColumns = @JoinColumn(name = "subbredit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> admins;

}
