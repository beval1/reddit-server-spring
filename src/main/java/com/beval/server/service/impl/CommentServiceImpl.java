package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.PageableDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @Override
    @Transactional
    public PageableDTO<CommentDTO> getAllCommentsForPostAndParentComment(String postId, String commentId,
                                                                         UserPrincipal userPrincipal, Pageable pageable) {

//        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        PostEntity post = postRepository.findById(Long.parseLong(postId))
                .orElseThrow(ResourceNotFoundException::new);

        CommentEntity parentComment = null;
        //if it's not comment but reply
        if (commentId != null) {
            parentComment = commentRepository.findById(Long.parseLong(commentId))
                    .orElseThrow(ResourceNotFoundException::new);
        }
        UserEntity userEntity = null;
        if (userPrincipal != null) {
            userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                    .orElseThrow(NotAuthorizedException::new);
        }
        Page<CommentEntity> commentEntities = commentRepository
                .findAllCommentsByPostAndParentComment(post, parentComment, pageable);
        List<CommentDTO> commentDTOS = Arrays.asList(modelMapper.map(commentEntities.getContent(), CommentDTO[].class));

        for (int i = 0; i < commentDTOS.size(); i++) {
            //set upvoted and downvoted attributes of DTO
            votingUtility.setUpvotedAndDownvotedForUser(commentEntities.getContent().get(i), commentDTOS.get(i), userEntity);
            //set upvotes and downvotes for DTO
            votingUtility.setVotes(commentEntities.getContent().get(i), commentDTOS.get(i));

            Page<CommentEntity> repliesEntities = commentRepository
                    .findAllCommentsByPostAndParentComment(post, commentEntities.getContent().get(i), pageable);
            List<CommentDTO> repliesDTOs = Arrays.asList(modelMapper.map(repliesEntities.getContent(), CommentDTO[].class));

            //set size of third level replies and replies voted status for the principal
            for (int k = 0; k < repliesDTOs.size(); k++) {
                int repliesCount = commentRepository
                        .countAllByPostAndParentComment(post, repliesEntities.getContent().get(k));
                repliesDTOs.get(k).setRepliesCount(repliesCount);
                //set upvoted and downvoted attributes of DTO
                votingUtility.setUpvotedAndDownvotedForUser(repliesEntities.getContent().get(k), repliesDTOs.get(k), userEntity);
                //set upvotes and downvotes for DTO
                votingUtility.setVotes(repliesEntities.getContent().get(k), repliesDTOs.get(k));
            }

            //append to parentComment DTO
            commentDTOS.get(i).setReplies(repliesDTOs);
            commentDTOS.get(i).setRepliesCount(repliesDTOs.size());
        }

        return PageableDTO
                .<CommentDTO>builder()
                .pageContent(commentDTOS)
                .pageNo(commentEntities.getNumber() + 1)
                .pageSize(commentEntities.getSize())
                .pageElements(commentDTOS.size())
                .totalElements(commentEntities.getTotalElements())
                .totalPages(commentEntities.getTotalPages())
                .last(commentEntities.isLast())
                .first(commentEntities.isFirst())
                .sortedBy(pageable.getSort().toString())
                .build();
    }

    @Override
    public void createComment(String postId, @NotNull CreateCommentDTO createCommentDTO, @NotNull UserPrincipal userPrincipal) {
        PostEntity postEntity = postRepository.findById(Long.parseLong(postId))
                .orElseThrow(ResourceNotFoundException::new);
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        //don't allow any comments on archived posts
        if (postEntity.isArchived()) {
            throw new ResourceArchivedException();
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
    public void createReply(String commentId, @NotNull CreateCommentDTO createCommentDTO, @NotNull UserPrincipal userPrincipal) {
        CommentEntity commentEntity = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Parent comment not found!"));
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        commentRepository.save(
                CommentEntity
                        .builder()
                        .parentComment(commentEntity)
                        .content(createCommentDTO.getContent())
                        .author(userEntity)
                        .post(commentEntity.getPost())
                        .build()
        );
    }

    @Override
    @Transactional
    public void updateCommentOrReply(String commentId, @NotNull CreateCommentDTO createCommentDTO) {
        CommentEntity commentEntity = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(ResourceNotFoundException::new);

        //Don't allow update if comment is archived
        if (commentEntity.isArchived()) {
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
