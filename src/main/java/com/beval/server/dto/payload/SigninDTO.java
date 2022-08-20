package com.beval.server.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class SigninDTO {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;
}
