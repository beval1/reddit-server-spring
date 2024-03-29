package com.beval.server.api.v1;

import com.beval.server.ResetDatabase;
import com.beval.server.ResetDatabaseTestExecutionListener;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.model.entity.*;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
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
@Transactional
class CommentControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubredditRepository subredditRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private PostEntity post1;
    private CommentEntity testCommentWithReplies;
    private CommentEntity comment2;
    private CommentEntity comment_post2;
    private CommentEntity archived_comment;
    private PostEntity archived_post;

    @BeforeEach
    public void setUp() {
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

        userRepository.save(
                UserEntity
                        .builder()
                        .username("test_user2")
                        .password(pass)
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .birthdate(null)
                        .firstName("Test")
                        .lastName("Test")
                        .email("test2@test2.com")
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

        SubredditEntity subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .moderators(Set.of(SubredditModerator))
                        .name("SubredditName")
                        .description("new subreddit description with enough characters")
                        .bannedUsers(Set.of(bannedUser))
                        .build()
        );

        post1 = postRepository.save(
                PostEntity
                        .builder()
                        .title("First test post")
                        .author(testUser)
                        .subreddit(subreddit)
                        .type("text")
                        .build()
        );

        testCommentWithReplies = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post1)
                        .content("First test comment")
                        .author(testUser)
                        .parentComment(null)
                        .build()
        );

        comment2 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post1)
                        .content("Second test comment")
                        .author(testUser)
                        .parentComment(null)
                        .build()
        );
        CommentEntity comment3 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post1)
                        .content("Third test comment")
                        .author(testUser)
                        .parentComment(null)
                        .build()
        );
        CommentEntity reply1 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post1)
                        .content("First reply to a comment")
                        .author(testUser)
                        .parentComment(testCommentWithReplies)
                        .build()
        );

        commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post1)
                        .content("Reply to reply")
                        .author(testUser)
                        .parentComment(reply1)
                        .build()
        );

        PostEntity post2 = postRepository.save(
                PostEntity
                        .builder()
                        .title("Second test post")
                        .author(testUser)
                        .subreddit(subreddit)
                        .type("text")
                        .build()
        );

        comment_post2 = comment2 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post2)
                        .content("Comment for post2")
                        .author(testUser)
                        .parentComment(null)
                        .build()
        );

        archived_post = postRepository.save(
                PostEntity
                        .builder()
                        .title("Archived test post")
                        .author(testUser)
                        .subreddit(subreddit)
                        .type("text")
                        .build()
        );

         archived_comment = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(archived_post)
                        .content("Archived comment for post2")
                        .author(testUser)
                        .parentComment(null)
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
//    }

    @Test
    void getAllCommentsForPost() throws Exception {
        mockMvc.perform(get(API_BASE + "/comments/" + post1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.pageContent", hasSize(3)));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void isCommentUpvotedByUser() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment_post2.getId() + "/upvote"))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BASE + "/comments/" + comment_post2.getPost().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.pageContent[0].upvotedByUser", Matchers.is(true)));
    }

    @Test
    void getAllRepliesForComment() throws Exception {
        mockMvc.perform(get(API_BASE + "/comments/" + post1.getId() + "/comment/" + testCommentWithReplies.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.pageContent", hasSize(1)))
                .andExpect(jsonPath("$.content.pageContent[0].replies", hasSize(1)));
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createComment() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/" + post1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithAnonymousUser
    void createComment_WhenAnonymous_IsUnauthorized() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/" + post1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createComment_WhenUserIsBanned_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/" + post1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createComment_WhenPostIsArchived_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/" + archived_post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createReply() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    void createReply_WhenAnonymous_IsUnauthorized() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createReply_WhenUserIsBanned_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createReply_WhenParentCommentIsArchived_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("test comment content")
                .build();

        mockMvc.perform(post(API_BASE + "/comments/comment/" + archived_comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void updateCommentOrReply_WhenAnonymous_IsUnauthorized() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("updated comment content")
                .build();

        mockMvc.perform(patch(API_BASE + "/comments/comment/" + testCommentWithReplies.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateCommentOrReply_WhenOwner_WorksCorrectly() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("updated comment content")
                .build();

        mockMvc.perform(patch(API_BASE + "/comments/comment/" + testCommentWithReplies.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = "test_user2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateCommentOrReply_WhenNotOwner_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("updated comment content")
                .build();

        mockMvc.perform(patch(API_BASE + "/comments/comment/" + testCommentWithReplies.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateCommentOrReply_WhenUserIsBanned_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("updated comment content")
                .build();

        mockMvc.perform(patch(API_BASE + "/comments/comment/" + testCommentWithReplies.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateCommentOrReply_WhenParentCommentIsArchived_IsForbidden() throws Exception {
        CommentDTO commentDTO = CommentDTO
                .builder()
                .content("updated comment content")
                .build();

        mockMvc.perform(patch(API_BASE + "/comments/comment/" + archived_comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteCommentOrReply_WhenOwner_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/comments/comment/" + testCommentWithReplies.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void deleteCommentOrReply_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(delete(API_BASE + "/comments/comment/" + testCommentWithReplies.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteCommentOrReply_WhenNotOwner_IsForbidden() throws Exception {
        mockMvc.perform(delete(API_BASE + "/comments/comment/" + testCommentWithReplies.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "subreddit_moderator", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteCommentOrReply_WhenSubredditModerator_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/comments/comment/" + testCommentWithReplies.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "application_admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteCommentOrReply_WhenApplicationAdmin_WorksCorrectly() throws Exception {
        mockMvc.perform(delete(API_BASE + "/comments/comment/" + testCommentWithReplies.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void upvoteComment() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/upvote"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void upvoteComment_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/upvote"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void downVoteComment() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/downvote"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void downVoteComment_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/downvote"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unvoteComment() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/unvote"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void unvoteComment_WhenAnonymous_IsUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/unvote"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unvoteComment_WhenUserIsBanned_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/unvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void downVoteComment_WhenUserIsBanned_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/downvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "banned_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void upvoteComment_WhenUserIsBanned_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + comment2.getId() + "/upvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unvoteComment_WhenCommentIsArchived_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + archived_comment.getId() + "/unvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void downVoteComment_WhenCommentIsArchived_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + archived_comment.getId() + "/downvote"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test_user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void upvoteComment_WhenCommentIsArchived_IsForbidden() throws Exception {
        mockMvc.perform(post(API_BASE + "/comments/comment/" + archived_comment.getId() + "/upvote"))
                .andExpect(status().isForbidden());
    }
}