package com.beval.server.dto.payload;

import com.beval.server.utils.validators.SubredditNameValidator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import static com.beval.server.config.AppConstants.MAXIMUM_SUBREDDIT_DESCRIPTION_LENGTH;
import static com.beval.server.config.AppConstants.MINIMUM_SUBREDDIT_DESCRIPTION_LENGTH;

@Getter
@Setter
public class CreateSubredditDTO {
    @SubredditNameValidator
    private String title;
    @NotNull
    @Length(max = MAXIMUM_SUBREDDIT_DESCRIPTION_LENGTH, min = MINIMUM_SUBREDDIT_DESCRIPTION_LENGTH)
    private String description;
}
