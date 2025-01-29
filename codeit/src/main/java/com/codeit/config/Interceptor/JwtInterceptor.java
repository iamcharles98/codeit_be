package com.codeit.config.Interceptor;


import com.codeit.jwt.JwtTokenProvider;
import com.codeit.jwt.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {


    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);
    private final List<String> allowedUrls = List.of(
            "/favicon.ico", "/user/login", "/user/register", "/swagger-ui", "/v3/api-docs" ,"/user/check"
    );
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info(request.getRequestURI());

        if(isAllowed(request.getRequestURI())) {
            return true;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String token = TokenExtractor.extractJwt(request);
        jwtTokenProvider.validateToken(token);

        request.setAttribute("userId", jwtTokenProvider.getUserIdFromToken(token));
        return true;
    }

    private boolean isAllowed(String url) {
        return allowedUrls.stream().anyMatch(url::startsWith);
    }
}
