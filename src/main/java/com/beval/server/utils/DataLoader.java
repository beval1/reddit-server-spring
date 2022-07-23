package com.beval.server.utils;

import com.beval.server.model.entity.*;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@ConditionalOnProperty("app.dataLoader")
public class DataLoader implements ApplicationRunner {
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RoleRepository roleRepository;

    public DataLoader(SubredditRepository subredditRepository, UserRepository userRepository,
                      PostRepository postRepository, CommentRepository commentRepository,
                      RoleRepository roleRepository) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void run(ApplicationArguments args) {
        //set user roles
        RoleEntity adminRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
        RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());

        UserEntity user = userRepository.save(
                UserEntity
                        .builder()
                        .username("test1")
                        .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
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
                        .username("admin")
                        .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                        .enabled(true)
                        .roles(Set.of(adminRole))
                        .birthdate(null)
                        .firstName("Admin")
                        .lastName("Adminov")
                        .email("admin@admin.com")
                        .build()
        );

        SubredditEntity subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .admins(List.of(user))
                        .name("Reddit title")
                        .description("tralalalallalallalallalallalalalalalallalalallalalal")
                        .build()
        );

        PostEntity post = postRepository.save(
            PostEntity
                    .builder()
                    .title("Testov post1111")
                    .author(user)
                    .subreddit(subreddit)
                    .build()
        );

        PostEntity post2 = postRepository.save(
                PostEntity
                        .builder()
                        .title("vtori testov post")
                        .author(user)
                        .subreddit(subreddit)
                        .build()
        );
        post2.setCreatedOn(LocalDateTime.of(2020, 1, 1, 1, 1));

        CommentEntity commentEntity = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post)
                        .content("lalalalal")
                        .author(user)
                        .parentComment(null)
                        .build()
        );
        CommentEntity comment2 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post)
                        .content("bababababababa")
                        .author(user)
                        .parentComment(null)
                        .build()
        );
        CommentEntity comment3 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post)
                        .content("hahahahaahahahaha")
                        .author(user)
                        .parentComment(null)
                        .build()
        );
        CommentEntity reply1 = commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post)
                        .content("22222222222222")
                        .author(user)
                        .parentComment(commentEntity)
                        .build()
        );

        commentRepository.save(
                CommentEntity
                        .builder()
                        .post(post)
                        .content("3333333333333333333")
                        .author(user)
                        .parentComment(reply1)
                        .build()
        );

    }
}
