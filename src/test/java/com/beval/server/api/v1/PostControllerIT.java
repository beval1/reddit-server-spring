package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
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

import java.util.List;
import java.util.Set;

import static com.beval.server.config.AppConstants.API_BASE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class PostControllerIT {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubredditRepository subredditRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private SubredditEntity subreddit;
    private PostEntity post;

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

        subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .admins(List.of(subredditAdmin))
                        .name("Subreddit title test")
                        .description("new subreddit description with enough characters")
                        .build()
        );

        post = postRepository.save(
                PostEntity
                        .builder()
                        .author(testUser)
                        .subreddit(subreddit)
                        .title("New post title")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        subredditRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllPostsForSubreddit() throws Exception {
        mockMvc.perform(get(API_BASE + "/posts/" + subreddit.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.pageContent", hasSize(1)));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void isPostUpvotedByUser() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/upvote"))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BASE + "/posts/" + subreddit.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.pageContent", hasSize(1)))
                .andExpect(jsonPath("$.content.pageContent[0].upvotedByUser", Matchers.is(true)));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createPost_WhenLoggedIn_WorksCorrectly() throws Exception {
        CreatePostDTO createPostDTO = CreatePostDTO
                .builder()
                .title("My new post title")
                .originalComment(CreateCommentDTO
                        .builder()
                        .content("comment content")
                        .build())
                .build();

        log.info(objectMapper.writeValueAsString(createPostDTO));

        mockMvc.perform(post(API_BASE + "/posts/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    void createPost_WhenAnonymous_IsUnauthorized() throws Exception {
        CreatePostDTO createPostDTO = CreatePostDTO
                .builder()
                .title("My new post title")
                .originalComment(CreateCommentDTO
                        .builder()
                        .content("comment content")
                        .build())
                .build();

        mockMvc.perform(post(API_BASE + "/posts/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletePost_WhenUserIsOwner_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/posts/post/" + post.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void deletePost_WhenUserIsAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(delete(API_BASE + "/posts/post/" + post.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "subreddit_admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletePost_WhenUserIsSubredditAdmin_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/posts/post/" + post.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "application_admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletePost_WhenUserIsAppAdmin_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/posts/post/" + post.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void upvotePost() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/upvote"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void upvotePost_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/upvote"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void downvotePost() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/downvote"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void downvotePost_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/downvote"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unvotePost() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/unvote"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void unvotePost_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post.getId() + "/unvote"))
                .andExpect(status().isUnauthorized());
    }
}