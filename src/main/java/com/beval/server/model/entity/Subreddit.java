package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subreddits")
public class Subreddit extends BaseEntity{
    private String title;

    @OneToMany
    private List<PostEntity> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "subbreddit_admins",
            joinColumns = @JoinColumn(name = "subbredit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> admins;

}
