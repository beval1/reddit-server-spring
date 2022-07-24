package com.beval.server.api.v1;

import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.dto.response.UserProfileDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(value = API_BASE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/my-profile")
    public ResponseEntity<ResponseDTO> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        MyProfileDTO myProfileDTO = userService.getMyProfile(userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(myProfileDTO)
                                .build()

                );
    }

    @GetMapping(value = "/users/user/{userId}")
    public ResponseEntity<ResponseDTO> getUserProfile(@PathVariable(value = "userId") String userId) {
        UserProfileDTO userProfileDTO = userService.getUserProfile(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(userProfileDTO)
                                .build()

                );
    }

    @PostMapping(value = "/users/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> setUserProfileImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @RequestParam(required = false) String title,
                                                           @RequestPart MultipartFile file) {

        userService.setProfileImage(userPrincipal, ImageUploadPayloadDTO
                .builder()
                .title(title)
                .multipartFile(file)
                .build());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Profile Image set successfully!")
                                .build()

                );
    }

    @PostMapping(value = "/users/banner-image")
    public ResponseEntity<ResponseDTO> setUserBannerImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @RequestParam(required = false) String title,
                                                          @RequestPart MultipartFile file) {
        userService.setBannerImage(userPrincipal, ImageUploadPayloadDTO
                .builder()
                .title(title)
                .multipartFile(file)
                .build());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Banner Image set successfully!")
                                .build()

                );
    }

    @DeleteMapping(value = "/users/profile-image")
    public ResponseEntity<ResponseDTO> deleteUserProfileImage(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteProfileImage(userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Profile Image deleted successfully!")
                                .build()

                );
    }

    @DeleteMapping(value = "/users/banner-image")
    public ResponseEntity<ResponseDTO> deleteUserBannerImage(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteBannerImage(userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Banner Image deleted successfully!")
                                .build()

                );
    }

}
