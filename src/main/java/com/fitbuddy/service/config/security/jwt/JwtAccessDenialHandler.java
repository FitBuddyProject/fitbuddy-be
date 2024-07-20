package com.fitbuddy.service.config.security.jwt;

import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Configuration(value = "jwt-access-denial-handler")
public class JwtAccessDenialHandler implements AccessDeniedHandler {
    private HandlerExceptionResolver resolver;

    @Autowired
    public JwtAccessDenialHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, new UnsupportedJwtException(accessDeniedException.getMessage(), accessDeniedException.getCause()));
    }
}
