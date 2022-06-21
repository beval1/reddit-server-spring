package com.beval.reddit_server_spring.config;

import com.beval.reddit_server_spring.model.entity.Comment;
import com.beval.reddit_server_spring.model.entity.Post;
import com.beval.reddit_server_spring.model.entity.Subreddit;
import com.beval.reddit_server_spring.model.entity.User;
import com.beval.reddit_server_spring.repository.PostCommentRepository;
import com.beval.reddit_server_spring.repository.PostRepository;
import com.beval.reddit_server_spring.repository.SubredditRepository;
import com.beval.reddit_server_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;

    @Autowired
    public DataLoader(SubredditRepository subredditRepository, UserRepository userRepository, PostRepository postRepository, PostCommentRepository commentRepository) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void run(ApplicationArguments args) {
        User user = userRepository.save(new User());
        Comment comment = commentRepository.save(Comment.builder().author(user).content("lalalalal").build());
        Post post = postRepository.save(new Post("Kur", user, List.of(comment)));
        Subreddit subreddit = subredditRepository.saveAndFlush(new Subreddit("Gluposti",
                List.of(post), List.of(user)));

    }
}
