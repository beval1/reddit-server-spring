package com.beval.server.dto.payload;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
}
