package com.fitbuddy.service.config.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final ModelMapper mapper;

    private static final String SECRET_KEY = "FITBUDDY_SIGNKEY";

    public <T extends JwtEncryptable> String encrypt(T target) {
        Map<String, Object> map = Map.of(
               "uuid" , target.getUuid(),
               "id" , target.getId(),
               "joinDate", target.getJoinDate(),
               "lastModifiedDate" , target.getLastModifiedDate()
        );

        LocalDateTime now = LocalDateTime.now();
        Date dateNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime sevenDaysAfter = now.plusDays(7);
        Date dateSevenDaysAfter = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder().setClaims(map)
                   .setIssuer("fitBuddy")
                   .setId(target.getUuid())
                   .setExpiration(dateNow)
                   .setNotBefore(dateNow)
                   .setIssuedAt(dateSevenDaysAfter)
                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                   .compact();
    }

    public <T extends JwtEncryptable> Authentication decrypt(String token, Class<T> classType) {
        Jwt result =  Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJwt(token);
        T principal = mapper.map(result.getBody(), classType);
        return new UsernamePasswordAuthenticationToken(principal, token, Set.of());
    }



}
