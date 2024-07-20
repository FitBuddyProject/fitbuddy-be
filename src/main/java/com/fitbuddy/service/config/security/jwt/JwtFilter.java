package com.fitbuddy.service.config.security.jwt;

import com.fitbuddy.service.repository.entity.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


import java.io.IOException;
import java.util.List;

import static com.fitbuddy.service.config.constants.Constants.AUTHORIZATION;
import static com.fitbuddy.service.config.constants.Constants.BEARER_PREFIX;
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private List<String> path;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String path = httpServletRequest.getServletPath();

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
        Authentication authentication = tokenProvider.decrypt(jwt, User.class);
        return authentication;
    }

    private Boolean isByPass ( String path ) {
        return path.contains(path);
    }
}
