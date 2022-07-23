package com.beval.server.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SigninDTO {
    private String usernameOrEmail;
    private String password;
}
