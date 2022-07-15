package com.beval.server.dto.payload;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignupDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
