package com.beval.server.service;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.dto.payload.UpdateUserProfileDTO;
import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.dto.response.UserProfileDTO;
import com.beval.server.security.UserPrincipal;

public interface UserService {
    UserProfileDTO getUserProfile(Long userId);

    MyProfileDTO getMyProfile(UserPrincipal userPrincipal);

    void setProfileImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO);
    void setBannerImage(UserPrincipal userPrincipal, ImageUploadPayloadDTO imageUploadDTO);

    void deleteBannerImage(UserPrincipal userPrincipal);
    void deleteProfileImage(UserPrincipal userPrincipal);

    void updateMyProfile(UserPrincipal userPrincipal, UpdateUserProfileDTO updateUserProfileDTO);

    void banUserFromSubreddit(UserPrincipal principal, Long userId, Long subredditId);

    void banUserFromApp(UserPrincipal principal, Long userId);

    void deleteMyProfile(UserPrincipal principal);
}
