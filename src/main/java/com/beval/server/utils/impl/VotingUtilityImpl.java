package com.beval.server.utils.impl;

import com.beval.server.dto.interfaces.UpvotableDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.interfaces.Upvotable;
import com.beval.server.repository.CommentRepository;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.utils.VotingUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VotingUtilityImpl implements VotingUtility {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public VotingUtilityImpl(CommentRepository commentRepository, PostRepository postRepository,
                             UserRepository userRepository) {

        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void vote(String entityId, @NotNull UserPrincipal userPrincipal, String action, @NotNull String entityType) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        Upvotable upvotableEntity = null;
        if (entityType.equals("post")){
            upvotableEntity =  postRepository.findById(Long.parseLong(entityId)).
                    orElseThrow(ResourceNotFoundException::new);
        } else if(entityType.equals("comment")){
            upvotableEntity = commentRepository.findById(Long.parseLong(entityId)).
                    orElseThrow(ResourceNotFoundException::new);
        } else {
            throw new ResourceNotFoundException();
        }

        switch (action) {
            case "upvote" -> {
                if (upvotableEntity.getDownvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getDownvotedUsers().remove(userEntity);
                }
                upvotableEntity.getUpvotedUsers().add(userEntity);
            }
            case "downvote" -> {
                if (upvotableEntity.getUpvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getUpvotedUsers().remove(userEntity);
                }
                upvotableEntity.getDownvotedUsers().add(userEntity);
            }
            case "unvote" -> {
                if (upvotableEntity.getDownvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getDownvotedUsers().remove(userEntity);
                }
                if (upvotableEntity.getUpvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getUpvotedUsers().remove(userEntity);
                }
            }
            default -> throw new IllegalArgumentException();
        }
    }

    //sets downvoted and upvoted attributes of CommentDTO
    public void setUpvotedAndDownvotedForUser(@NotNull Upvotable upvotable,
                                               UpvotableDTO upvotableDTO,
                                               UserEntity user) {

        if(user == null){
            return;
        }

        if (upvotable.getUpvotedUsers().stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            upvotableDTO.setUpvotedByUser(true);
        }
        if (upvotable.getDownvotedUsers().stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            upvotableDTO.setDownvotedByUser(true);
        }
    }

    public void setVotes(@NotNull Upvotable upvotable,
                                 @NotNull UpvotableDTO upvotableDTO){
        upvotableDTO.setVotes(upvotable.getUpvotedUsers().size() - upvotable.getDownvotedUsers().size());
    }
}
