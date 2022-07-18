package com.beval.server.utils.impl;

import com.beval.server.dto.interfaces.UpvotableDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceArchivedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.interfaces.Upvotable;
import com.beval.server.repository.UpvotableRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.utils.VotingUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VotingUtilityImpl implements VotingUtility {
    private final UserRepository userRepository;
    private final UpvotableRepository upvotableRepository;


    public VotingUtilityImpl(UserRepository userRepository, UpvotableRepository upvotableRepository) {
        this.userRepository = userRepository;
        this.upvotableRepository = upvotableRepository;
    }

    public void vote(String entityId, @NotNull UserPrincipal userPrincipal, String action) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        Upvotable upvotableEntity = upvotableRepository.findById(Long.parseLong(entityId))
                .orElseThrow(ResourceNotFoundException::new);

        //archived posts and comments shouldn't be upvoted
        if(upvotableEntity.isArchived()){
            throw new ResourceArchivedException();
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
            //that would be developer mistake
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
