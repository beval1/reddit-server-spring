package com.beval.server.utils.impl;

import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.*;
import com.beval.server.repository.*;
import com.beval.server.security.UserPrincipal;
import com.beval.server.utils.SecurityExpressionUtility;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SecurityExpressionUtilityImpl implements SecurityExpressionUtility {

    private final UserRepository userRepository;
    private final UpvotableRepository upvotableRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;

    public SecurityExpressionUtilityImpl(UserRepository userRepository, UpvotableRepository upvotableRepository,
                                         CommentRepository commentRepository, PostRepository postRepository,
                                         SubredditRepository subredditRepository) {
        this.userRepository = userRepository;
        this.upvotableRepository = upvotableRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
    }

    @Override
    public boolean isResourceOwner(Long resourceId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        UpvotableEntity upvotable = upvotableRepository.findById(resourceId).orElseThrow(ResourceNotFoundException::new);

        return upvotable.getAuthor().getId().equals(loggedUser.getId());
    }

    @Override
    @Transactional
    public boolean isSubredditModeratorOfComment(Long commentId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(ResourceNotFoundException::new);

        return commentEntity.getPost().getSubreddit().getModerators()
                .stream()
                .anyMatch(moderator -> moderator.getUsername().equals(loggedUser.getUsername()));
    }

    @Override
    @Transactional
    public boolean isSubredditModeratorOfPost(Long postId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(ResourceNotFoundException::new);

        return postEntity.getSubreddit().getModerators()
                .stream()
                .anyMatch(moderator -> moderator.getUsername().equals(loggedUser.getUsername()));
    }

    @Override
    @Transactional
    public boolean isSubredditModerator(Long subredditId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId).orElseThrow(ResourceNotFoundException::new);

        return subredditEntity.getModerators()
                .stream()
                .anyMatch(moderator -> moderator.getUsername().equals(loggedUser.getUsername()));
    }

    @Override
    @Transactional
    public boolean isUserBannedFromSubreddit(Long subredditId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        SubredditEntity subreddit = subredditRepository.findById(subredditId).orElseThrow(ResourceNotFoundException::new);

        return subreddit.getBannedUsers().stream().anyMatch(bannedUser ->
                bannedUser.getUsername().equals(loggedUser.getUsername()));
    }

    @Override
    public boolean isResourceArchived(Long resourceId) {
        UpvotableEntity upvotable = upvotableRepository.findById(resourceId).orElseThrow(ResourceNotFoundException::new);
        return upvotable.isArchived();
    }

}
