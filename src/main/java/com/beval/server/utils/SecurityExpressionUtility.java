package com.beval.server.utils;

import com.beval.server.security.UserPrincipal;

public interface SecurityExpressionUtility {
    boolean isResourceOwner(Long resourceId, UserPrincipal userPrincipal);
    boolean isSubredditAdminOfComment(Long resourceId, UserPrincipal userPrincipal);
    boolean isSubredditAdminOfPost(Long resourceId, UserPrincipal userPrincipal);
}
