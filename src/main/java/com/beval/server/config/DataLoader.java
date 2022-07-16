package com.beval.server.config;

import com.beval.server.model.entity.*;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.RoleRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.service.CommentService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final RoleRepository roleRepository;
    private final CommentService commentService;

    public DataLoader(SubredditRepository subredditRepository, UserRepository userRepository,
                      PostRepository postRepository,
                      RoleRepository roleRepository, CommentService commentService) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.commentService = commentService;
    }

    @Transactional
    public void run(ApplicationArguments args) {
        //set user roles
        roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
        RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());
        roleRepository.save(RoleEntity.builder().roleName(RoleEnum.MODERATOR).build());

        List<RoleEntity> userRolesList = List.of(userRole);

        UserEntity user = userRepository.save(
                UserEntity
                        .builder()
                        .username("test1")
                        .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                        .enabled(true)
                        .roles(userRolesList)
                        .birthdate(null)
                        .firstName("Test")
                        .lastName("Test")
                        .email("test@test.com")
                        .build()
        );

        SubredditEntity subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .admins(List.of(user))
                        .title("Reddit title")
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

        CommentEntity commentEntity = commentService.addComment("lalalalal", post, user);
        CommentEntity reply1 = commentService.addReply("222222222", post, user, commentEntity);
        commentService.addReply("333333333", post, user, reply1);

    }
}
