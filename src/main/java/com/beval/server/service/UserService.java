package com.beval.server.service;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.dto.response.UserProfileDTO;
import com.beval.server.security.UserPrincipal;

public interface UserService {
    UserProfileDTO getUserProfile(String userId);

    MyProfileDTO getMyProfile(UserPrincipal userPrincipal);

    void setProfileImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO);
    void setBannerImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO);

    void deleteBannerImage(UserPrincipal userPrincipal);
    void deleteProfileImage(UserPrincipal userPrincipal);
}
