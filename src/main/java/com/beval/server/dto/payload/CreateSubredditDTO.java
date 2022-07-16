package com.beval.server.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubredditDTO {
    private String title;
    private String description;
}
