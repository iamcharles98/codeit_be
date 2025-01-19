package com.codeit.config.filter;

import com.codeit.util.BaseException;
import com.codeit.util.ErrorType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
@Order(1)
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            // BaseException 발생 시 커스텀 예외 처리
            resolver.resolveException(request,response,null,e);
        } catch (Exception e) {
            // 기타 예외 발생 시 처리
            resolver.resolveException(request,response,null,new BaseException(ErrorType.UNKNOWN_ERROR));
        }
    }
}
