package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceArchivedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.CommentRepository;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CommentService;
import com.beval.server.utils.VotingUtility;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final VotingUtility votingUtility;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                              UserRepository userRepository, ModelMapper modelMapper,
                              VotingUtility votingUtility) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.votingUtility = votingUtility;
    }

    //TODO: make this function recursive
    @Override
    @Transactional
    public List<CommentDTO> getAllCommentsForPostAndParentComment(String postId, String commentId,
                                                                  UserPrincipal userPrincipal) {

        PostEntity post = postRepository.findById(Long.parseLong(postId))
                .orElseThrow(ResourceNotFoundException::new);

        CommentEntity parentComment = null;
        //if it's not comment but reply
        if (commentId != null) {
            parentComment = commentRepository.findById(Long.parseLong(commentId))
                    .orElseThrow(ResourceNotFoundException::new);
        }
        UserEntity userEntity = null;
        if (userPrincipal != null){
            userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                    .orElseThrow(NotAuthorizedException::new);
        }
        List<CommentEntity> commentEntities = commentRepository
                .findAllCommentsByPostAndParentComment(post, parentComment);
        List<CommentDTO> commentDTOS = Arrays.asList(modelMapper.map(commentEntities, CommentDTO[].class));

        for (int i = 0; i < commentDTOS.size(); i++) {
            //set upvoted and downvoted attributes of DTO
            votingUtility.setUpvotedAndDownvotedForUser(commentEntities.get(i), commentDTOS.get(i), userEntity);
            //set upvotes and downvotes for DTO
            votingUtility.setVotes(commentEntities.get(i), commentDTOS.get(i));

            List<CommentEntity> repliesEntities = commentRepository
                    .findAllCommentsByPostAndParentComment(post, commentEntities.get(i));
            List<CommentDTO> repliesDTOs = Arrays.asList(modelMapper.map(repliesEntities, CommentDTO[].class));

            //set size of third level replies and replies voted status for the principal
            for (int k = 0; k < repliesDTOs.size(); k++) {
                int repliesCount = commentRepository
                        .countAllByPostAndParentComment(post, repliesEntities.get(k));
                repliesDTOs.get(k).setRepliesCount(repliesCount);
                //set upvoted and downvoted attributes of DTO
                votingUtility.setUpvotedAndDownvotedForUser(repliesEntities.get(k), repliesDTOs.get(k), userEntity);
                //set upvotes and downvotes for DTO
                votingUtility.setVotes(repliesEntities.get(k), repliesDTOs.get(k));
            }

            //append to parentComment DTO
            commentDTOS.get(i).setReplies(repliesDTOs);
            commentDTOS.get(i).setRepliesCount(repliesDTOs.size());
        }
        return commentDTOS;
    }

    @Override
    public void createComment(String postId, @NotNull CreateCommentDTO createCommentDTO, @NotNull UserPrincipal userPrincipal) {
        PostEntity postEntity = postRepository.findById(Long.parseLong(postId))
                .orElseThrow(ResourceNotFoundException::new);
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        //don't allow any comments on archived posts
        if (postEntity.isArchived()){
            throw  new ResourceArchivedException();
        }

        commentRepository.save(
                CommentEntity
                        .builder()
                        .parentComment(null)
                        .content(createCommentDTO.getContent())
                        .author(userEntity)
                        .post(postEntity)
                        .build()
        );
    }

    @Override
    public void createReply(String commentId, String postId, @NotNull CreateCommentDTO createCommentDTO, @NotNull UserPrincipal userPrincipal) {
        PostEntity postEntity = postRepository.findById(Long.parseLong(postId))
                .orElseThrow(ResourceNotFoundException::new);
        CommentEntity commentEntity = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(ResourceNotFoundException::new);
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        commentRepository.save(
                CommentEntity
                        .builder()
                        .parentComment(commentEntity)
                        .content(createCommentDTO.getContent())
                        .author(userEntity)
                        .post(postEntity)
                        .build()
        );
    }

    @Override
    @Transactional
    public void updateCommentOrReply(String commentId, @NotNull CreateCommentDTO createCommentDTO) {
        CommentEntity commentEntity = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(ResourceNotFoundException::new);

        //Don't allow update if comment is archived
        if (commentEntity.isArchived()){
            throw new ResourceArchivedException();
        }

        commentEntity.setContent(createCommentDTO.getContent());
    }

    @Override
    @Transactional
    public void deleteCommentOrReply(String commentId) {
        Long commentIdLong = Long.parseLong(commentId);
        CommentEntity commentEntity = commentRepository.findById(commentIdLong)
                .orElseThrow(ResourceNotFoundException::new);

        //first check if there are any replies to the comment
        if (commentRepository.existsByParentComment(commentEntity)) {
            commentEntity.setContent("[deleted]");
        } else {
            commentRepository.deleteById(commentIdLong);
        }

    }

    @Override
    @Transactional
    public void upvoteComment(String commentId, @NotNull UserPrincipal userPrincipal) {
        votingUtility.vote(commentId, userPrincipal, "upvote");
    }

    @Override
    @Transactional
    public void downvoteComment(String commentId, @NotNull UserPrincipal userPrincipal) {
        votingUtility.vote(commentId, userPrincipal, "downvote");
    }

    @Override
    @Transactional
    public void unvoteComment(String commentId, @NotNull UserPrincipal userPrincipal) {
        votingUtility.vote(commentId, userPrincipal, "unvote");
    }


}
