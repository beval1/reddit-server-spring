package com.beval.server.config;

import java.util.List;

public final class AppConstants {
    private AppConstants(){}
    public static final String API_BASE = "/api/v1";
    public static final List<String> DO_NOT_FILTER_PATHS = List.of(
            AppConstants.API_BASE + "/auth/signin",
            AppConstants.API_BASE + "/auth/signup"
    );

    public static final int MINIMUM_TITLE_LENGTH = 10;
    public static final int MAXIMUM_TITLE_LENGTH = 80;
    public static final int MAXIMUM_USERNAME_LENGTH = 20;
    public static final int MINIMUM_USERNAME_LENGTH = 5;


}
