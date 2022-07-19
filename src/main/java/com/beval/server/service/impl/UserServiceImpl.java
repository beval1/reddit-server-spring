package com.beval.server.service.impl;

import com.beval.server.repository.UpvotableRepository;
import com.beval.server.repository.UserRepository;
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


}
