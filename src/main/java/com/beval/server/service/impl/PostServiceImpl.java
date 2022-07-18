package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.CommentRepository;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.PostService;
import com.beval.server.utils.VotingUtility;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final VotingUtility votingUtility;
    public PostServiceImpl(PostRepository postRepository, SubredditRepository subredditRepository,
                           CommentRepository commentRepository, UserRepository userRepository,
                           ModelMapper modelMapper, VotingUtility votingUtility) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.votingUtility = votingUtility;
    }

    @Override
    @Transactional
    public List<PostDTO> getAllPostsForSubreddit(String subredditId, UserPrincipal userPrincipal) {
        SubredditEntity subredditEntity = subredditRepository.findById(Long.parseLong(subredditId))
                .orElseThrow(ResourceNotFoundException::new);
        //user isn't required to be logged in
        UserEntity userEntity = null;
        if (userPrincipal != null) {
            userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                    userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        }

        List<PostEntity> postEntities = postRepository.findAllBySubredditOrderByCreatedOn(subredditEntity);
        List<PostDTO> postDTOS = Arrays.asList(modelMapper.map(postEntities, PostDTO[].class));
        for (int i = 0; i < postDTOS.size(); i++) {
            votingUtility.setUpvotedAndDownvotedForUser(postEntities.get(i), postDTOS.get(i), userEntity);
            votingUtility.setVotes(postEntities.get(i), postDTOS.get(i));
        }
        return postDTOS;
    }

    @Override
    public void createPostForSubreddit(@NotNull CreatePostDTO createPostDTO, @NotNull UserPrincipal userPrincipal, String subredditId) {
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
        commentRepository.save(
                CommentEntity
                        .builder()
                        .post(postEntity)
                        .content(createPostDTO.getOriginalComment().getContent())
                        .author(userEntity)
                        .parentComment(null)
                        .build()
        );
    }

    @Override
    @Transactional
    public void deletePost(String postId) {
        //delete all comments
        commentRepository.deleteAllByPostId(Long.parseLong(postId));

        //delete the post itself
        postRepository.deleteById(Long.parseLong(postId));
    }

    @Override
    @Transactional
    public void upvotePost(String postId, UserPrincipal userPrincipal) {
        votingUtility.vote(postId, userPrincipal, "upvote");
    }

    @Override
    @Transactional
    public void downvotePost(String postId, UserPrincipal userPrincipal) {
        votingUtility.vote(postId, userPrincipal, "downvote");
    }

    @Override
    @Transactional
    public void unvotePost(String postId, UserPrincipal userPrincipal) {
        votingUtility.vote(postId, userPrincipal, "unvote");
    }

}
