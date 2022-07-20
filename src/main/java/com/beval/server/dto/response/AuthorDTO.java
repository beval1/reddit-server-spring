package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDTO {
    private Long id;
    private String username;
    private ImageDTO profileImage;
    private int postKarma;
    private int commentKarma;
    private int awardeeKarma;
    private int awarderKarma;
}
