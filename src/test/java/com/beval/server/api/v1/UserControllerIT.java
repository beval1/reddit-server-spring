package com.beval.server.api.v1;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.dto.payload.UpdateUserProfileDTO;
import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.model.entity.ImageEntity;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.ImageRepository;
import com.beval.server.repository.RoleRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.service.CloudinaryService;
import com.beval.server.service.impl.CloudinaryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.beval.server.config.AppConstants.API_BASE;
import static com.beval.server.config.AppConstants.DEFAULT_USER_PROFILE_IMAGE_CLOUDINARY_FOLDER;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CloudinaryService cloudinaryService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        RoleEntity adminRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
        RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());

        String pass = passwordEncoder.encode("1234");

        testUser = userRepository.save(
                UserEntity
                        .builder()
                        .username("test_user")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .birthdate(null)
                        .firstName("Test")
                        .lastName("Test")
                        .email("test@test.com")
                        .build()
        );

        UserEntity SubredditModerator =  userRepository.save(
                UserEntity
                        .builder()
                        .username("subreddit_moderator")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .birthdate(null)
                        .firstName("Test")
                        .lastName("Test")
                        .email("subreddit_moderator@sub.com")
                        .build()
        );

        UserEntity appAdmin =  userRepository.save(
                UserEntity
                        .builder()
                        .username("application_admin")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(adminRole))
                        .birthdate(null)
                        .firstName("Admin")
                        .lastName("Admin")
                        .email("admin@admin.com")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getMyProfile_WhenLoggedIn_WorksCorrectly() throws Exception {
        mockMvc.perform(get(API_BASE + "/users/my-profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.username", Matchers.is(testUser.getUsername())));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateMyProfile_WhenLoggedIn_WorksCorrectly() throws Exception {
        UpdateUserProfileDTO updateUserProfileDTO= UpdateUserProfileDTO
                .builder()
                .birthdate(LocalDate.now())
                .firstName("Updated firstname")
                .lastName("Updated lastname")
                .username("new_username")
                .build();

        mockMvc.perform(patch(API_BASE + "/users/my-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserProfileDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void updateMyProfile_WhenAnonymous_IsUnauthorized() throws Exception {
        UpdateUserProfileDTO updateUserProfileDTO= UpdateUserProfileDTO
                .builder()
                .birthdate(LocalDate.now())
                .firstName("Updated firstname")
                .lastName("Updated lastname")
                .username("new_username")
                .build();
        mockMvc.perform(patch(API_BASE + "/users/my-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserProfileDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void getMyProfile_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(get(API_BASE + "/users/my-profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserProfile() throws Exception {
        mockMvc.perform(get(API_BASE + "/users/user/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.username", Matchers.is(testUser.getUsername())));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateProfileImage_WhenLoggedIn_WorksCorrectly() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        mockMvc.perform(multipart(API_BASE + "/users/profile-image")
                        .file(file)
                        .param("title", "test image title"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void updateProfileImage_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/users/profile-image"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateBannerImage_WhenLoggedIn_WorksCorrectly() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        mockMvc.perform(multipart(API_BASE + "/users/banner-image")
                        .file(file)
                        .param("title", "test image title"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void updateBannerImage_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/users/banner-image"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteProfileImage_WhenLoggedIn_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/users/profile-image"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void deleteProfileImage_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(delete(API_BASE + "/users/profile-image"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteBannerImage_WhenLoggedIn_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/users/banner-image"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void deleteBannerImage_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(delete(API_BASE + "/users/banner-image"))
                .andExpect(status().isUnauthorized());
    }
}