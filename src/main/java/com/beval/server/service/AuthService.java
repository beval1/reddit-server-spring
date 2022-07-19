package com.beval.server.service;

import com.beval.server.dto.payload.SigninDTO;
import com.beval.server.dto.payload.SignupDTO;

public interface AuthService {
    String signInUser(SigninDTO signinDto);
    void signUpUser(SignupDTO signupDto);
}
