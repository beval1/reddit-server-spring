package com.beval.server.service.impl;

import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.dto.response.UserProfileDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserProfileDTO getUserProfile(String userId) {
        UserEntity userEntity = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(NotAuthorizedException::new);

        return modelMapper.map(userEntity, UserProfileDTO.class);
    }

    @Override
    public MyProfileDTO getMyProfile(UserPrincipal userPrincipal) {
        if (userPrincipal == null){
            throw new NotAuthorizedException();
        }

        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                        userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        return modelMapper.map(userEntity, MyProfileDTO.class);
    }
}
