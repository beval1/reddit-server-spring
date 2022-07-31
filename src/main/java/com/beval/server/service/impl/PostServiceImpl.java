package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.exception.UserBannedException;
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
import com.beval.server.utils.SecurityExpressionUtility;
import com.beval.server.utils.VotingUtility;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SecurityExpressionUtility securityExpressionUtility;

    public PostServiceImpl(PostRepository postRepository, SubredditRepository subredditRepository,
                           CommentRepository commentRepository, UserRepository userRepository,
                           ModelMapper modelMapper, VotingUtility votingUtility,
                           SecurityExpressionUtility securityExpressionUtility) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.votingUtility = votingUtility;
        this.securityExpressionUtility = securityExpressionUtility;
    }

    @Override
    @Transactional
    public PageableDTO<PostDTO> getAllPostsForSubreddit(Long subredditId, UserPrincipal userPrincipal, Pageable pageable) {
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);
        //user isn't required to be logged in
        UserEntity userEntity = null;
        if (userPrincipal != null) {
            userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                    userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        }

        Page<PostEntity> postEntities = postRepository.findAllBySubreddit(subredditEntity, pageable);
        List<PostDTO> postDTOS = Arrays.asList(modelMapper.map(postEntities.getContent(), PostDTO[].class));
        for (int i = 0; i < postDTOS.size(); i++) {
            votingUtility.setUpvotedAndDownvotedForUser(postEntities.getContent().get(i), postDTOS.get(i), userEntity);
            votingUtility.setVotes(postEntities.getContent().get(i), postDTOS.get(i));
        }

        return PageableDTO
                .<PostDTO>builder()
                .pageContent(postDTOS)
                .pageNo(postEntities.getNumber() + 1)
                .pageSize(postEntities.getSize())
                .pageElements(postDTOS.size())
                .totalElements(postEntities.getTotalElements())
                .totalPages(postEntities.getTotalPages())
                .last(postEntities.isLast())
                .first(postEntities.isFirst())
                .sortedBy(pageable.getSort().toString())
                .build();
    }

    @Override
    public void createPostForSubreddit(@NotNull CreatePostDTO createPostDTO, @NotNull UserPrincipal userPrincipal, Long subredditId) {
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);

        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        if(securityExpressionUtility.isUserBannedFromSubreddit(subredditEntity.getId(), userPrincipal)){
            throw new UserBannedException();
        }

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
    public void deletePost(Long postId) {
        //delete all comments
        commentRepository.deleteAllByPostId(postId);

        //delete the post itself
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void upvotePost(Long postId, UserPrincipal userPrincipal) {
        votingUtility.vote(postId, userPrincipal, "upvote");
    }

    @Override
    @Transactional
    public void downvotePost(Long postId, UserPrincipal userPrincipal) {
        votingUtility.vote(postId, userPrincipal, "downvote");
    }

    @Override
    @Transactional
    public void unvotePost(Long postId, UserPrincipal userPrincipal) {
        votingUtility.vote(postId, userPrincipal, "unvote");
    }

}
