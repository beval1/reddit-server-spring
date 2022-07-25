package com.beval.server.utils;

import com.beval.server.security.UserPrincipal;

public interface SecurityExpressionUtility {
    boolean isResourceOwner(Long resourceId, UserPrincipal userPrincipal);
    boolean isSubredditModeratorOfPost(Long postId, UserPrincipal userPrincipal);
    boolean isSubredditModeratorOfComment(Long commentId, UserPrincipal userPrincipal);
    boolean isSubredditModerator(Long subredditId, UserPrincipal userPrincipal);
    boolean isUserBannedFromSubreddit(Long subredditId, UserPrincipal userPrincipal);
    boolean isResourceArchived(Long resourceId);
}
