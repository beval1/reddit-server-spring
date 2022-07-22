package com.beval.server.api.v1;

import com.beval.server.dto.payload.SigninDTO;
import com.beval.server.dto.payload.SignupDTO;
import com.beval.server.dto.response.JwtResponseDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(path = API_BASE)
public class AuthController {

    private final AuthService userService;

    public AuthController(AuthService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<ResponseDTO> signIn(@RequestBody SigninDTO signinDto) {

        String token = userService.signInUser(signinDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Logged in successfully!")
                                .content(JwtResponseDTO
                                        .builder()
                                        .accessToken(token)
                                        .tokenType("Bearer").build())
                                .build()
                );
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody SignupDTO signupDto) {
        userService.signUpUser(signupDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Signed up successfully!")
                                .content(null)
                                .build()
                );
    }
}
