package com.beval.server.service.impl;

import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.interfaces.Upvotable;
import com.beval.server.repository.UpvotableRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UpvotableRepository upvotableRepository;

    public UserServiceImpl(UserRepository userRepository, UpvotableRepository upvotableRepository) {
        this.userRepository = userRepository;
        this.upvotableRepository = upvotableRepository;
    }

    @Override
    public boolean isOwner(Long resourceId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        Upvotable upvotable = upvotableRepository.findById(resourceId).orElseThrow(ResourceNotFoundException::new);

        return upvotable.getAuthor().getId().equals(loggedUser.getId());
    }
}
