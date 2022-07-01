package com.beval.server.service;

import com.beval.server.dto.payload.SigninDto;
import com.beval.server.dto.payload.SignupDto;

public interface UserService {
    String signInUser(SigninDto signinDto);
    void signUpUser(SignupDto signupDto);
}
