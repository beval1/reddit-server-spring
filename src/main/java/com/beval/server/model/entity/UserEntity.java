package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.beval.server.config.AppConstants.MAXIMUM_USERNAME_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_USERNAME_LENGTH;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserEntity extends BaseEntity {
    @NotNull
    @Length(max = MAXIMUM_USERNAME_LENGTH, min = MINIMUM_USERNAME_LENGTH)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    private LocalDate birthdate;
    @OneToOne
    private ImageEntity profileImage;
    @OneToOne
    private ImageEntity bannerImage;
    private int postKarma;
    private int commentKarma;
    private int awardeeKarma;
    private int awarderKarma;
    private boolean enabled;
    private boolean locked;
    private boolean accountExpired;
    private boolean credentialsExpired;


    // when user is deleted, delete the corresponding roles in the mapping table
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    @ManyToMany
    @JoinTable(name="users_upvotes", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<PostEntity> upvotedPosts;

    @ManyToMany
    @JoinTable(name="users_downvotes", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<PostEntity> downvotedPosts;

}
