package com.fitbuddy.service.config.security.jwt;

import com.fitbuddy.service.config.security.Properties;
import com.fitbuddy.service.repository.dto.UserDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static com.fitbuddy.service.config.constants.Constants.AUTHORIZATION;
import static com.fitbuddy.service.config.constants.Constants.BEARER_PREFIX;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final Properties properties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Authentication authentication = this.validateToken(httpServletRequest);
        if(Objects.nonNull(authentication))  SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String path = httpServletRequest.getServletPath();
        if(!StringUtils.hasText(path)) path =  httpServletRequest.getPathInfo();
        return this.isByPass(path);
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
