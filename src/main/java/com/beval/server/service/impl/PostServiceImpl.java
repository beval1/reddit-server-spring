package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CommentService;
import com.beval.server.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CommentService commentService;

    public PostServiceImpl(PostRepository postRepository, SubredditRepository subredditRepository,
                           UserRepository userRepository, ModelMapper modelMapper, CommentService commentService) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.commentService = commentService;
    }

    @Override
    public List<PostDTO> getAllPostsForSubreddit(String subredditId) {
        SubredditEntity subredditEntity = subredditRepository.findById(Long.parseLong(subredditId))
                .orElseThrow(ResourceNotFoundException::new);

        List<PostEntity> postEntities = postRepository.findAllBySubredditOrderByCreatedOn(subredditEntity);
        return Arrays.asList(modelMapper.map(postEntities, PostDTO[].class));
    }

    @Override
    public void createPostForSubreddit(CreatePostDTO createPostDTO, UserPrincipal userPrincipal, String subredditId) {
        SubredditEntity subredditEntity = subredditRepository.findById(Long.parseLong(subredditId))
                .orElseThrow(ResourceNotFoundException::new);

        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        //create the post
        PostEntity postEntity = postRepository.save(
                PostEntity
                    .builder()
                        .title(createPostDTO.getTitle())
                        .subreddit(subredditEntity)
                        .author(userEntity)
                    .build()
        );

        //create OP comment
        commentService.addComment(createPostDTO.getOriginalComment().getContent(), postEntity, userEntity);
    }
}
