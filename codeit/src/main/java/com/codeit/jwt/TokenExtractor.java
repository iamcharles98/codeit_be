package com.codeit.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class TokenExtractor {
    private static final String SCHEMA = "Bearer ";
    private static final String TOKEN_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Authorization-Refresh";


    public static String extractJwt(HttpServletRequest req) {
        String token = req.getHeader(TOKEN_HEADER);

        if (token != null && token.startsWith(SCHEMA)) {
            return token.replace(SCHEMA,"");
        }
        throw new RuntimeException("invalid token");
    }
    public static String extractRefresh(HttpServletRequest req) {
        String token = req.getHeader(REFRESH_HEADER);

        if (token != null && token.startsWith(SCHEMA)) {
            return token.replace(SCHEMA,"");
        }
        throw new RuntimeException("invalid token");
    }
}
