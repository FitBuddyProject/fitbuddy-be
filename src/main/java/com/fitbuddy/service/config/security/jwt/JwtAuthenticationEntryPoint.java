package com.fitbuddy.service.config.security.jwt;

import io.jsonwebtoken.IncorrectClaimException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component(value = "jwt-authentication-entry-point")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private HandlerExceptionResolver resolver;

    @Autowired //handlerExceptionResolver를 Qualifer로 지정해서 ControllerAdvice에서 처리하도록 위임
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("JwtAuthenticationEntryPoint");
        resolver.resolveException(request, response, null, new IncorrectClaimException(null, null, authException.getMessage(), authException.getCause()));
    }

}
