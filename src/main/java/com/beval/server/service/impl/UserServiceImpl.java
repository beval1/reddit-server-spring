package com.beval.server.service.impl;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.dto.payload.UpdateUserProfileDTO;
import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.dto.response.UserProfileDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.ImageEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CloudinaryService;
import com.beval.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.beval.server.config.AppConstants.DEFAULT_USER_BANNER_IMAGE_CLOUDINARY_FOLDER;
import static com.beval.server.config.AppConstants.DEFAULT_USER_PROFILE_IMAGE_CLOUDINARY_FOLDER;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SubredditRepository subredditRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public UserServiceImpl(UserRepository userRepository, SubredditRepository subredditRepository, ModelMapper modelMapper,
                           CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }


    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        //throw ResourceNotFoundException here if user isn't found because the user is what we are looking
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(ResourceNotFoundException::new);

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

    @Override
    @Transactional
    public void setProfileImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        //first upload the new image
        ImageEntity imageEntity = cloudinaryService.upload(imageUploadDTO,
                DEFAULT_USER_PROFILE_IMAGE_CLOUDINARY_FOLDER);

        //if the new image is uploaded
        if (imageEntity != null && userEntity.getProfileImage() != null){
            //delete the old image
            cloudinaryService.delete(userEntity.getProfileImage());
        }

        //set the new profile image
        userEntity.setProfileImage(imageEntity);
    }

    @Override
    @Transactional
    public void setBannerImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        ImageEntity imageEntity = cloudinaryService.upload(imageUploadDTO,
                DEFAULT_USER_BANNER_IMAGE_CLOUDINARY_FOLDER);

        if (imageEntity != null && userEntity.getBannerImage() != null){
            cloudinaryService.delete(userEntity.getBannerImage());
        }

        userEntity.setBannerImage(imageEntity);
    }

    @Override
    @Transactional
    public void deleteProfileImage(UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        ImageEntity profileImage = userEntity.getProfileImage();
        //delete user-image relation
        userEntity.setProfileImage(null);
        //delete image in cloudinary and the database
        cloudinaryService.delete(profileImage);
    }

    @Override
    public void updateMyProfile(UserPrincipal userPrincipal, UpdateUserProfileDTO updateUserProfileDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        userEntity.setUsername(updateUserProfileDTO.getUsername());
        userEntity.setBirthdate(updateUserProfileDTO.getBirthdate());
        userEntity.setFirstName(updateUserProfileDTO.getFirstName());
        userEntity.setLastName(updateUserProfileDTO.getLastName());
    }

    @Override
    @Transactional
    public void deleteBannerImage(UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        ImageEntity bannerImage = userEntity.getBannerImage();
        //delete user-image relation
        userEntity.setBannerImage(null);
        //delete image in cloudinary and the database
        cloudinaryService.delete(bannerImage);
    }

    @Override
    @Transactional
    public void banUserFromSubreddit(UserPrincipal principal, Long userId, Long subredditId) {
        UserEntity userForBan = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "User not found!"));
        SubredditEntity subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);

        subreddit.getBannedUsers().add(userForBan);
    }

    @Override
    @Transactional
    public void banUserFromApp(UserPrincipal principal, Long userId) {
        UserEntity userForBan = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "User not found!"));
        userForBan.setEnabled(false);
    }
}
