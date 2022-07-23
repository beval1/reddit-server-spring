package com.beval.server.api.v1;

import com.beval.server.dto.payload.SigninDTO;
import com.beval.server.dto.payload.SignupDTO;
import com.beval.server.model.entity.RoleEntity;
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
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    void setUp() {
        roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void signIn() throws Exception {
        authService.signUpUser(
                SignupDTO
                .builder()
                .username("test1")
                .email("test_email1@test.com")
                .firstName("Test")
                .lastName("Testov")
                .password("1234")
                .build()
        );

        mockMvc.perform(post(API_BASE+"/auth/signin")
                        .content(objectMapper.writeValueAsString(new SigninDTO("test1", "1234")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.accessToken").exists());
    }

    @Test
    void signUp() throws Exception {
        SignupDTO newUser = SignupDTO
                .builder()
                .username("test2")
                .email("test_email@test.com")
                .firstName("Test")
                .lastName("Testov")
                .password("1234")
                .build();
        mockMvc.perform(post(API_BASE+"/auth/signup")
                        .content(objectMapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}