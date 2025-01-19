package com.codeit.config.filter;

import com.codeit.jwt.JwtTokenProvider;
import com.codeit.jwt.TokenExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final List<String> allowedUrls = List.of(
            "/user/login", "/user/register", "/swagger-ui", "/v3/api-docs"
    );
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url = request.getRequestURI();

        if (isAllowed(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = TokenExtractor.extractJwt(request);
        jwtTokenProvider.validateToken(token);

        filterChain.doFilter(request, response);
    }


    private boolean isAllowed(String url) {
        return allowedUrls.stream().anyMatch(url::startsWith);
    }
}
