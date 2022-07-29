package com.beval.server.model.entity;

import com.beval.server.utils.validators.EmailValidator;
import com.beval.server.utils.validators.UserUsernameValidator;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserEntity extends BaseEntity {
    @UserUsernameValidator
    private String username;
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
    @EmailValidator
    private String email;
    private LocalDate birthdate;
    @OneToOne
    private ImageEntity profileImage;
    @OneToOne
    private ImageEntity bannerImage;
    private double postKarma;
    private double commentKarma;
    private double awardeeKarma;
    private double awarderKarma;
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean locked = false;
    @Builder.Default
    private boolean accountExpired = false;
    @Builder.Default
    private boolean credentialsExpired = false;


    // when user is deleted, delete the corresponding roles in the mapping table
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

//    @ManyToMany
//    @JoinTable(name="user_post_upvotes",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id"))
//    @Builder.Default
//    private Set<PostEntity> upvotedPosts = new HashSet<>();
//
//    @ManyToMany
//    @JoinTable(name="user_post_downvotes",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id"))
//    @Builder.Default
//    private Set<PostEntity> downvotedPosts = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @Builder.Default
    private Set<SubredditEntity> subreddits = new HashSet<>();

}
