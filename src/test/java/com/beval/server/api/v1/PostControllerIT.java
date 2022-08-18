package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import java.util.Set;

import static com.beval.server.config.AppConstants.API_BASE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
    private PostEntity archived_post;
    private PostEntity post_new_sub;
    private SubredditEntity new_sub;

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

        UserEntity bannedUser =  userRepository.save(
                UserEntity
                        .builder()
                        .username("banned_user")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(adminRole))
                        .birthdate(null)
                        .firstName("Banned")
                        .lastName("User")
                        .email("banned@user.com")
                        .build()
        );

        subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .moderators(Set.of(SubredditModerator))
                        .name("SubredditName")
                        .description("new subreddit description with enough characters")
                        .build()
        );

        post = postRepository.save(
                PostEntity
                        .builder()
                        .author(testUser)
                        .subreddit(subreddit)
                        .title("New post title")
                        .type("text")
                        .build()
        );

        new_sub = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .moderators(Set.of(SubredditModerator))
                        .name("NewSub")
                        .description("new subreddit description with enough characters")
                        .bannedUsers(Set.of(bannedUser))
                        .build()
        );

        post_new_sub = postRepository.save(
                PostEntity
                        .builder()
                        .author(testUser)
                        .subreddit(new_sub)
                        .title("New post title")
                        .type("text")
                        .build()
        );

        archived_post = postRepository.save(
                PostEntity
                        .builder()
                        .author(testUser)
                        .subreddit(new_sub)
                        .title("New post title")
                        .type("text")
                        .archived(true)
                        .build()
        );
    }

//    @AfterEach
//    void tearDown() {
//        commentRepository.deleteAll();
//        postRepository.deleteAll();
//        subredditRepository.deleteAll();
//        userRepository.deleteAll();
//        roleRepository.deleteAll();
//    }

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
                .andExpect(jsonPath("$.content.pageContent[0].upvotedByUser", Matchers.is(true)));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createTextPost_WhenLoggedIn_WorksCorrectly() throws Exception {
        CreatePostDTO createPostDTO = CreatePostDTO
                .builder()
                .title("My new post title")
                .originalComment(CreateCommentDTO
                        .builder()
                        .content("comment content")
                        .build())
                .build();

        mockMvc.perform(post(API_BASE + "/posts/text-post/" + subreddit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createTextPost_WhenUserIsBanned_IsForbidden() throws Exception {
        CreatePostDTO createPostDTO = CreatePostDTO
                .builder()
                .title("My new post title")
                .originalComment(CreateCommentDTO
                        .builder()
                        .content("comment content")
                        .build())
                .build();

        mockMvc.perform(post(API_BASE + "/posts/text-post/" + new_sub.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createTextPost_WhenAnonymous_IsUnauthorized() throws Exception {
        CreatePostDTO createPostDTO = CreatePostDTO
                .builder()
                .title("My new post title")
                .originalComment(CreateCommentDTO
                        .builder()
                        .content("comment content")
                        .build())
                .build();

        mockMvc.perform(post(API_BASE + "/posts/text-post/" + subreddit.getId())
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
    @WithUserDetails(value = "subreddit_moderator", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletePost_WhenUserIsSubredditModerator_WorksCorrectly() throws Exception {
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


    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unvotePost_WhenUserIsBanned_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post_new_sub.getId() + "/unvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void downVotePost_WhenUserIsBanned_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post_new_sub.getId() + "/downvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void upvotePost_WhenUserIsBanned_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + post_new_sub.getId() + "/upvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unvotePost_WhenPostIsArchived_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + archived_post.getId() + "/unvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void downVotePost_WhenPostIsArchived_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + archived_post.getId() + "/downvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void upvotePost_WhenPostIsArchived_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/posts/post/" + archived_post.getId() + "/upvote"))
                .andExpect(status().isForbidden());
    }
}