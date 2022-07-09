package com.beval.server.config;

import java.util.List;

public final class AppConstants {
    private AppConstants(){}
    public static final String API_BASE = "/api/v1";
    public static final List<String> DO_NOT_FILTER_PATHS = List.of(
            AppConstants.API_BASE + "/auth/signin",
            AppConstants.API_BASE + "/auth/signup"
    );


}
