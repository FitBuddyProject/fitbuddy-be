package com.fitbuddy.service.config.security.jwt;

import com.fitbuddy.service.config.security.Properties;
import com.fitbuddy.service.repository.dto.UserDto;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.fitbuddy.service.config.constants.Constants.AUTHORIZATION;
import static com.fitbuddy.service.config.constants.Constants.BEARER_PREFIX;
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Properties properties;



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String path = httpServletRequest.getServletPath();
        if(!StringUtils.hasText(path)) path =  httpServletRequest.getPathInfo();

        if( !this.isByPass(path) ) {
            Authentication authentication = this.validateToken(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
    private Authentication validateToken(HttpServletRequest request ) {

        String bearerToken = request.getHeader(AUTHORIZATION);
        if(
            !StringUtils.hasText(bearerToken) ||
            !bearerToken.startsWith(BEARER_PREFIX)
        )  throw new JwtException(null, null);

        String jwt =  bearerToken.replace(BEARER_PREFIX, "");
        Authentication authentication = tokenProvider.decrypt(jwt, UserDto.class);
        return authentication;
    }

    private Boolean isByPass ( String path ) {
        return properties.getIgnoreJwt().stream()
                  .filter(ignore -> antPathMatcher.match(ignore, path))
                  .count() > 0;
    }
}
