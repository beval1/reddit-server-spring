package com.beval.server.utils.impl;

import com.beval.server.dto.response.AbstractUpvotableDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceArchivedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.exception.UserBannedException;
import com.beval.server.model.entity.*;
import com.beval.server.repository.UpvotableRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.utils.SecurityExpressionUtility;
import com.beval.server.utils.VotingUtility;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.beval.server.config.AppConstants.*;

@Component
public class VotingUtilityImpl implements VotingUtility {
    private final UserRepository userRepository;
    private final UpvotableRepository upvotableRepository;
    private final SecurityExpressionUtility securityExpressionUtility;


    public VotingUtilityImpl(UserRepository userRepository, UpvotableRepository upvotableRepository,
                             SecurityExpressionUtility securityExpressionUtility) {
        this.userRepository = userRepository;
        this.upvotableRepository = upvotableRepository;
        this.securityExpressionUtility = securityExpressionUtility;
    }

    public void vote(Long entityId, @NotNull UserPrincipal userPrincipal, String action) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        UpvotableEntity upvotableEntity = upvotableRepository.findById(entityId)
                .orElseThrow(ResourceNotFoundException::new);

        SubredditEntity subredditEntity = null;
        if (upvotableEntity instanceof CommentEntity) {
            subredditEntity = ((CommentEntity) upvotableEntity).getPost().getSubreddit();
        } else if (upvotableEntity instanceof PostEntity){
            subredditEntity = ((PostEntity) upvotableEntity).getSubreddit();
        } else {
            //developer mistake
            throw new IllegalArgumentException();
        }


        //archived posts and comments shouldn't be upvoted
        if (upvotableEntity.isArchived()) {
            throw new ResourceArchivedException();
        }
        //banned users shouldn't be able to upvote
        if (securityExpressionUtility.isUserBannedFromSubreddit(subredditEntity.getId(), userPrincipal)) {
            throw new UserBannedException();
        }

        switch (action) {
            case "upvote" -> {
                if (upvotableEntity.getDownvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getDownvotedUsers().remove(userEntity);
                }
                upvotableEntity.getUpvotedUsers().add(userEntity);
                setUserKarma(upvotableEntity, action, userEntity);
            }
            case "downvote" -> {
                if (upvotableEntity.getUpvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getUpvotedUsers().remove(userEntity);
                }
                upvotableEntity.getDownvotedUsers().add(userEntity);
                setUserKarma(upvotableEntity, action, userEntity);
            }
            case "unvote" -> {
                if (upvotableEntity.getDownvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getDownvotedUsers().remove(userEntity);
                }
                if (upvotableEntity.getUpvotedUsers().stream().anyMatch(u -> u.getId().equals(userEntity.getId()))) {
                    upvotableEntity.getUpvotedUsers().remove(userEntity);
                }
                setUserKarma(upvotableEntity, action, userEntity);

            }
            //that would be developer mistake
            default -> throw new IllegalArgumentException();
        }
    }

    private void setUserKarma(UpvotableEntity upvotableEntity, String action, UserEntity userEntity){
        if (action.equals("upvote")){
            if (upvotableEntity instanceof CommentEntity) {
                upvotableEntity.getAuthor().setCommentKarma(upvotableEntity.getAuthor().getCommentKarma() +
                        (1*COMMENT_KARMA_UPVOTE_MULTIPLIER));
            } else if (upvotableEntity instanceof PostEntity){
                upvotableEntity.getAuthor().setPostKarma(upvotableEntity.getAuthor().getPostKarma() +
                        (1*POST_KARMA_UPVOTE_MULTIPLIER));
//                userEntity.getUpvotedPosts().add(((PostEntity) upvotableEntity));
            }
        } else if (action.equals("downvote")){
            if (upvotableEntity instanceof CommentEntity) {
                upvotableEntity.getAuthor().setCommentKarma(upvotableEntity.getAuthor().getCommentKarma() -
                        (1*COMMENT_KARMA_DOWNVOTE_MULTIPLIER));
            } else if (upvotableEntity instanceof PostEntity){
                upvotableEntity.getAuthor().setPostKarma(upvotableEntity.getAuthor().getPostKarma() -
                        (1*POST_KARMA_DOWNVOTE_MULTIPLIER));
//                userEntity.getDownvotedPosts().add(((PostEntity) upvotableEntity));
            }
        }
//        else {
//            if (upvotableEntity instanceof PostEntity){
//                userEntity.getDownvotedPosts().remove(upvotableEntity);
//                userEntity.getUpvotedPosts().remove(upvotableEntity);
//            }
//        }
    }

    //sets downvoted and upvoted attributes of CommentDTO
    public void setUpvotedAndDownvotedForUser(@NotNull UpvotableEntity upvotable,
                                              AbstractUpvotableDTO upvotableDTO,
                                              UserEntity user) {

        if (user == null) {
            return;
        }

        if (upvotable.getUpvotedUsers().stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            upvotableDTO.setUpvotedByUser(true);
        }
        if (upvotable.getDownvotedUsers().stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            upvotableDTO.setDownvotedByUser(true);
        }
    }

    public void setVotes(@NotNull UpvotableEntity upvotable,
                         @NotNull AbstractUpvotableDTO upvotableDTO) {
        upvotableDTO.setVotes(upvotable.getUpvotedUsers().size() - upvotable.getDownvotedUsers().size());
    }
}
