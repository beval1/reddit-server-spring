package com.beval.server.dto.payload;

import com.beval.server.utils.validators.EmailValidator;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignupDTO {
    @NotBlank
    private String username;
    @EmailValidator
    private String email;
    @NotBlank
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
