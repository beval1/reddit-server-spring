package com.beval.server.api.v1;

import com.beval.server.dto.payload.SigninDTO;
import com.beval.server.dto.payload.SignupDTO;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.RoleRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Set;

import static com.beval.server.config.AppConstants.API_BASE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());
        String pass = passwordEncoder.encode("1234");

        //pass 1234
        userRepository.save(
                UserEntity
                        .builder()
                        .username("test1")
                        .email("test_email@test.com")
                        .password(pass)
                        .firstName("Test")
                        .lastName("Test")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .build()
        );

        UserEntity lockedUser = userRepository.save(UserEntity
                .builder()
                .username("locked_user")
                .email("locked_user_email@test.com")
                .password(pass)
                .firstName("Test")
                .lastName("Test")
                .password(pass)
                .roles(Set.of(userRole))
                .enabled(true)
                .locked(true)
                .build()
        );

        UserEntity disabledUser = userRepository.save(UserEntity
                .builder()
                .username("disabled_user")
                .email("disabled_user_email@test.com")
                .password(pass)
                .firstName("Test")
                .lastName("Test")
                .password(pass)
                .roles(Set.of(userRole))
                .enabled(false)
                .build()
        );

        UserEntity credentialsExpiredUser = userRepository.save(UserEntity
                .builder()
                .username("credentials_expired")
                .email("credentials_expired_user@test.com")
                .password(pass)
                .firstName("Test")
                .lastName("Test")
                .password(pass)
                .roles(Set.of(userRole))
                .enabled(true)
                .credentialsExpired(true)
                .build()
        );

        UserEntity accountExpiredUser = userRepository.save(UserEntity
                .builder()
                .username("acc_expired")
                .email("account_expired_user@test.com")
                .password(pass)
                .firstName("Test")
                .lastName("Test")
                .password(pass)
                .roles(Set.of(userRole))
                .enabled(true)
                .accountExpired(true)
                .build()
        );

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void signIn() throws Exception {
        mockMvc.perform(post(API_BASE + "/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("test1", "1234")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.accessToken").exists());
    }

    @Test
    void signIn_WhenBadCredentials_IsBadRequest() throws Exception {
        mockMvc.perform(post(API_BASE + "/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("test1", "123456789")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signIn_WhenAccountLocked_IsBadRequest() throws Exception {
        mockMvc.perform(post(API_BASE + "/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("locked_user", "1234")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signIn_WhenAccountDisabled_IsBadRequest() throws Exception {
        mockMvc.perform(post(API_BASE + "/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("disabled_user", "1234")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signIn_WhenAccountCredentialsExpired_IsBadRequest() throws Exception {
        mockMvc.perform(post(API_BASE + "/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("credentials_expired", "1234")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signIn_WhenAccountExpired_IsBadRequest() throws Exception {
        mockMvc.perform(post(API_BASE + "/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("acc_expired", "1234")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp() throws Exception {
        SignupDTO newUser = SignupDTO
                .builder()
                .username("test2")
                .email("test_email2@test.com")
                .firstName("Test")
                .lastName("Test")
                .password("1234")
                .build();
        mockMvc.perform(post(API_BASE + "/auth/signup")
                        .content(objectMapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void signUp_WhenUsernameExists_IsBadRequest() throws Exception {
        SignupDTO newUser = SignupDTO
                .builder()
                .username("test1")
                .email("new_non_existent_email@email.com")
                .firstName("Test")
                .lastName("Test")
                .password("1234")
                .build();

        mockMvc.perform(post(API_BASE + "/auth/signup")
                        .content(objectMapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_WhenEmailExists_IsBadRequest() throws Exception {
        SignupDTO newUser = SignupDTO
                .builder()
                .username("new_username")
                .email("test_email@test.com")
                .firstName("Test")
                .lastName("Test")
                .password("1234")
                .build();

        mockMvc.perform(post(API_BASE + "/auth/signup")
                        .content(objectMapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}