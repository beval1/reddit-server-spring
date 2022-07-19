package com.beval.server.service;

import com.beval.server.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean isOwner(Long id, UserPrincipal userPrincipal);
}
