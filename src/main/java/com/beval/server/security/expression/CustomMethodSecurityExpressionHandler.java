package com.beval.server.security.expression;

import com.beval.server.service.UserService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final UserService userService;

    public CustomMethodSecurityExpressionHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
            CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(new AuthenticationTrustResolverImpl());
            root.setRoleHierarchy(getRoleHierarchy());
            root.setUserService(userService);
            return root;
    }
}
