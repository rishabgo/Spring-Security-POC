package com.restapi.springrestapi.util;

public class AppConstants {

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "5";
    public static final String SORT_BY = "postId";
    public static final String SORT_DIRECTION = "ASC";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String[] AUTH_WHITELIST = {
            "/h2-console/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

}
