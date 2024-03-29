package com.beval.server.config;

import java.util.List;

public final class AppConstants {
    private AppConstants(){}
    public static final String PROJECT_NAME = "Reddit Server";
    public static final String API_BASE = "/api/v1";
    public static final List<String> DO_NOT_FILTER_PATHS = List.of(
            AppConstants.API_BASE + "/auth/signin",
            AppConstants.API_BASE + "/auth/signup"
    );
    public static final List<String> CROSS_ORIGIN_DOMAINS = List.of(
            "http://localhost:3000",
            "http://127.0.0.1:80",
            "http://reddit-clone-react1.herokuapp.com",
            "https://reddit-clone-react1.herokuapp.com"
    );
    public static final int MAXIMUM_COMMENT_LENGTH = 255;
    public static final int MINIMUM_TITLE_LENGTH = 6;
    public static final int MAXIMUM_TITLE_LENGTH = 80;
    public static final int MAXIMUM_USERNAME_LENGTH = 20;
    public static final int MINIMUM_USERNAME_LENGTH = 5;
    public static final int MINIMUM_SUBREDDIT_NAME_LENGTH = 5;
    public static final int MAXIMUM_SUBREDDIT_NAME_LENGTH = 30;
    public static final int MAXIMUM_SUBREDDIT_DESCRIPTION_LENGTH = 350;
    public static final int MINIMUM_SUBREDDIT_DESCRIPTION_LENGTH = 20;
    public static final long POST_MONTHS_TO_ARCHIVE = 6;
    public static final String DEFAULT_USER_PROFILE_IMAGE_CLOUDINARY_FOLDER = PROJECT_NAME + "/users/profile-images";
    public static final String DEFAULT_USER_BANNER_IMAGE_CLOUDINARY_FOLDER = PROJECT_NAME + "/users/banner-images";
    public static final String DEFAULT_POST_IMAGES_CLOUDINARY_FOLDER = PROJECT_NAME + "/posts/images";
    public static final int PAGEABLE_DEFAULT_PAGE_NUMBER = 0;
    public  static final int PAGEABLE_DEFAULT_PAGE_SIZE = 20;

    public  static final double POST_KARMA_UPVOTE_MULTIPLIER = 0.6;
    public  static final double POST_KARMA_DOWNVOTE_MULTIPLIER = 1;
    public  static final double COMMENT_KARMA_UPVOTE_MULTIPLIER = 0.8;
    public  static final double COMMENT_KARMA_DOWNVOTE_MULTIPLIER = 1;


//    public static final String DEFAULT_USER_PROFILE_IMAGE_URL = "";
//    public static final String DEFAULT_USER_BANNER_IMAGE_URL = "";

}
