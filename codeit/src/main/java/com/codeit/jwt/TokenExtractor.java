package com.codeit.jwt;

import com.codeit.util.BaseException;
import com.codeit.util.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenExtractor {
    private static final String SCHEMA = "Bearer ";
    private static final String TOKEN_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Authorization-Refresh";


    public static String extractJwt(HttpServletRequest req) {
        String token = req.getHeader(TOKEN_HEADER);
        log.info(token);
        if (token != null && token.startsWith(SCHEMA)) {
            return token.replace(SCHEMA,"");
        }
        throw new BaseException(ErrorType.JWT_UNAUTHORIZED);
    }
    public static String extractRefresh(HttpServletRequest req) {
        String token = req.getHeader(REFRESH_HEADER);

        if (token != null && token.startsWith(SCHEMA)) {
            return token.replace(SCHEMA,"");
        }
        throw new BaseException(ErrorType.JWT_UNAUTHORIZED);
    }
}
