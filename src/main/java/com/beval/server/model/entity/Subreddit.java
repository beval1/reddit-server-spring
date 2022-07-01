package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Subreddit extends BaseEntity{
    private String title;

    @OneToMany
    private List<PostEntity> posts = new ArrayList<>();

    @ManyToMany
    private List<UserEntity> admins;

}
