package com.beval.server.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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
    @Length(max= MAXIMUM_USERNAME_LENGTH, min = MINIMUM_USERNAME_LENGTH)
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
    private boolean enabled;
    private boolean locked;
    private boolean accountExpired;
    private boolean credentialsExpired;


    // when user is deleted, delete the corresponding roles in the mapping table
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="users_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roles;

}
