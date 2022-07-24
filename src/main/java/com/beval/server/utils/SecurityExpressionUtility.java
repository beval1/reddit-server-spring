package com.beval.server.utils;

import com.beval.server.security.UserPrincipal;

public interface SecurityExpressionUtility {
    boolean isResourceOwner(Long resourceId, UserPrincipal userPrincipal);
    boolean isSubredditModeratorOfComment(Long resourceId, UserPrincipal userPrincipal);
    boolean isSubredditModeratorOfPost(Long resourceId, UserPrincipal userPrincipal);
    boolean isSubredditModerator(Long resourceId, UserPrincipal userPrincipal);
}
