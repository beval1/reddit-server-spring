package com.beval.server.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninDTO {
    private String usernameOrEmail;
    private String password;
}
