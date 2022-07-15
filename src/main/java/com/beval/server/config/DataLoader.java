package com.beval.server.config;

import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
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
    private final CommentRepository commentRepository;
    private final RoleRepository roleRepository;
    private final CommentService commentService;

    public DataLoader(SubredditRepository subredditRepository, UserRepository userRepository,
                      PostRepository postRepository, CommentRepository commentRepository,
                      RoleRepository roleRepository, CommentService commentService) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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
                UserEntity.builder().username("test1")
                .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                .enabled(true)
                        .roles(userRolesList)
                        .birthdate(null)
                        .firstName("Test")
                        .lastName("Test")
                        .email("test@test.com")
                .build());

        SubredditEntity subreddit = subredditRepository.save(
                SubredditEntity
                        .builder()
                        .admins(List.of(user))
                        .title("Reddit title")
                        .build());

        PostEntity post = postRepository.save(
            PostEntity
                    .builder()
                    .title("Testov post1111")
                    .author(user)
                    .subreddit(subreddit)
                    .build()
        );

        commentService.addComment("lalalalal", post, user);

    }
}
