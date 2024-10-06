package com.fitbuddy.service.config.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    private static final String SECRET_KEY = "FITBUDDYPROJECTSIGNKEYFITBUDDYPROJECTSIGNKEYFITBUDDYPROJECTSIGNKEY";

    public <T extends JwtEncryptable> String encrypt(T target, boolean isAccess) {
        target.encodeUUID();
        Map<String, Object> map = Map.of(
               "uuid" , target.getUuid(),
               "id" , target.getId(),
               "joinDate", target.getJoinDate(),
               "lastModifiedDate" , target.getLastModifiedDate()
        );

        LocalDateTime now = LocalDateTime.now();
        Date dateNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime sevenDaysAfter = now.plusDays(7);
        Date dateSevenDaysAfter = Date.from(sevenDaysAfter.atZone(ZoneId.systemDefault()).toInstant());


        Map<String, Object> header = Map.of("tokenType", isAccess ? "ACCESS" : "REFRESH");
        try {
            String json = objectMapper.writeValueAsString(map);

            return Jwts.builder()
                       .setClaims(Map.of("BODY", json))
                       .setIssuer("fitBuddy")
                       .setHeader(header)
                       .setId(target.getUuid())
                       .setExpiration(dateNow)
                       .setNotBefore(dateNow)
                       .setIssuedAt(dateSevenDaysAfter)
                       .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                       .compact();
        } catch (JsonProcessingException e) {
            throw new JwtException("유효하지 않은 claim");
        }
    }



    public <T extends JwtEncryptable> Authentication decrypt(String token, Class<T> classType) {
        Jwt result =  Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJwt(token);
        Map<String, String> body = (Map<String, String>) result.getBody();
        String json = body.get("BODY");
        try {
            T principal = objectMapper.readValue(json, classType);
            principal.decodeUUID();
            return new UsernamePasswordAuthenticationToken(principal, token, Set.of());
        } catch (JsonProcessingException e) {
            throw new JwtException("IllageToken");
        }
    }



}
