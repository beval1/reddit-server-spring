package com.beval.server.config;

import com.beval.server.model.entity.*;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(SubredditRepository subredditRepository, UserRepository userRepository,
                      PostRepository postRepository, PostCommentRepository commentRepository,
                      RoleRepository roleRepository) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
    }

    public void run(ApplicationArguments args) {
        //set user roles
        roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
        roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());
        roleRepository.save(RoleEntity.builder().roleName(RoleEnum.MODERATOR).build());

        UserEntity user = userRepository.save(UserEntity.builder().username("test")
                .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                .enabled(true)
                .build());
        CommentEntity comment = commentRepository.save(CommentEntity.builder().author(user).content("lalalalal").build());
        PostEntity post = postRepository.save(new PostEntity("Kur", user, List.of(comment)));
        subredditRepository.saveAndFlush(new Subreddit("Gluposti",
                List.of(post), List.of(user)));

    }
}
