package com.beval.server.api.v1;

import com.beval.server.dto.payload.SigninDto;
import com.beval.server.dto.payload.SignupDto;
import com.beval.server.dto.response.JwtResponseDto;
import com.beval.server.dto.response.SuccessDto;
import com.beval.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<SuccessDto> signIn(@RequestBody SigninDto signinDto) {

        String token = userService.signInUser(signinDto);

        return ResponseEntity.ok(
                SuccessDto
                        .builder()
                        .message("Logged in successfully!")
                        .status(HttpStatus.CREATED)
                        .content(JwtResponseDto
                                .builder()
                                .accessToken(token)
                                .tokenType("Bearer").build())
                        .build()
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessDto> signUp(@RequestBody SignupDto signupDto) {
        userService.signUpUser(signupDto);

        return ResponseEntity.ok(
                SuccessDto
                        .builder()
                        .message("Signed up successfully!")
                        .status(HttpStatus.OK)
                        .content(null)
                        .build()
        );
    }
}
