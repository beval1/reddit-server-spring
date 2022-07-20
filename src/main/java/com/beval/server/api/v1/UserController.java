package com.beval.server.api.v1;

import com.beval.server.dto.response.MyProfileDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.dto.response.UserProfileDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(value = API_BASE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/myprofile")
    public ResponseEntity<ResponseDTO> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal){
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
    public  ResponseEntity<ResponseDTO> getUserProfile(@PathVariable(value = "userId") String userId){
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

}
