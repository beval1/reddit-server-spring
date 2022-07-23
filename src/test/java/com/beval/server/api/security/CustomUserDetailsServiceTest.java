package com.beval.server.api.security;

import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.CustomUserDetailsService;
import com.beval.server.security.UserPrincipal;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private CustomUserDetailsService serviceTest;
    private ModelMapper modelMapper;
    private RoleEntity adminRole, userRole;
    private UserEntity testUser;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        serviceTest = new CustomUserDetailsService(mockUserRepository, modelMapper);
        userRole = new RoleEntity(RoleEnum.USER);
        adminRole = new RoleEntity(RoleEnum.ADMIN);
        testUser = UserEntity
                .builder()
                .username("test1")
                .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                .enabled(true)
                .roles(Set.of(userRole, adminRole))
                .birthdate(null)
                .firstName("Test")
                .lastName("Test")
                .email("test@test.com")
                .build();
    }

    @Test
    void loadUserByUsername_WhenUserIsNotFound_ThrowsException() {
        Assert.assertThrows(UsernameNotFoundException.class, () -> {
            serviceTest.loadUserByUsername("invalid_username_or_email_exceeding_maximum_characters");
        });
    }

    @Test
    void loadUserByUsername_WhenUserIsFound_WorksCorrectly() {
        Mockito.when(mockUserRepository.findByUsernameOrEmail(testUser.getEmail(), testUser.getEmail()))
                .thenReturn(Optional.ofNullable(testUser));

        UserPrincipal actual = serviceTest.loadUserByUsername(testUser.getEmail());
        assertEquals(actual.getUsername(), testUser.getUsername());
        String roles = "ROLE_USER, ROLE_ADMIN";
        String actualRoles = actual.getAuthorities().stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        assertEquals(roles, actualRoles);
    }

}