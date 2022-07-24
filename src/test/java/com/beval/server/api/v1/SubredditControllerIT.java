package com.beval.server.api.v1;

import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.RoleRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static com.beval.server.config.AppConstants.API_BASE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubredditControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SubredditRepository subredditRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private SubredditEntity subreddit;

    @BeforeEach
    void setUp() {
        RoleEntity adminRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
        RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());

        String pass = passwordEncoder.encode("1234");

        UserEntity testUser = userRepository.save(
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

        UserEntity subredditAdmin =  userRepository.save(
                UserEntity
                        .builder()
                        .username("subreddit_admin")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .birthdate(null)
                        .firstName("Test")
                        .lastName("Test")
                        .email("subreddit_admin@sub.com")
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

        this.subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .admins(List.of(subredditAdmin))
                        .name("SubredditName")
                        .description("new subreddit description with enough characters")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        subredditRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllSubreddits() throws Exception {
        mockMvc.perform(get(API_BASE + "/subreddits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.pageContent", hasSize(1)));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createSubreddit() throws Exception {
        SubredditDTO subredditDTO = SubredditDTO
                .builder()
                .title("AwesomeNewSubreddit")
                .description("example description of my newly created super awesome subreddit")
                .build();

        mockMvc.perform(post(API_BASE + "/subreddits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subredditDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    void createSubreddit_WhenUserIsAnonymous_IsUnauthorized() throws Exception {
        SubredditDTO subredditDTO = SubredditDTO
                .builder()
                .title("awesome new subreddit")
                .description("example description of my newly created super awesome subreddit")
                .build();

        mockMvc.perform(post(API_BASE + "/subreddits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subredditDTO))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "subreddit_admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateSubreddit_WhenUserIsSubredditAdmin_WorksCorrectly() throws Exception {
        SubredditDTO subredditDTO = SubredditDTO
                .builder()
                .title("NewSubredditName")
                .description("updated description of the old subreddit")
                .build();

        mockMvc.perform(patch(API_BASE + "/subreddits/subreddit/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subredditDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "application_admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateSubreddit_WhenUserIsAppAdmin_WorksCorrectly() throws Exception {
        SubredditDTO subredditDTO = SubredditDTO
                .builder()
                .title("NewSubredditName")
                .description("updated description of the old subreddit")
                .build();

        mockMvc.perform(patch(API_BASE + "/subreddits/subreddit/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subredditDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateSubreddit_WhenUserIsNotSubredditAdmin_IsForbidden() throws Exception {
        SubredditDTO subredditDTO = SubredditDTO
                .builder()
                .title("new subreddit name")
                .description("updated description of the old subreddit")
                .build();

        mockMvc.perform(patch(API_BASE + "/subreddits/subreddit/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subredditDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateSubreddit_WhenUserIsAnonymous_IsUnauthorized() throws Exception {
        SubredditDTO subredditDTO = SubredditDTO
                .builder()
                .title("new subreddit name")
                .description("updated description of the old subreddit")
                .build();

        mockMvc.perform(patch(API_BASE + "/subreddits/subreddit/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subredditDTO)))
                .andExpect(status().isUnauthorized());
    }
}