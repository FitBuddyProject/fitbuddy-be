package com.fitbuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitbuddy.service.config.security.jwt.TokenProvider;
import com.fitbuddy.service.repository.dto.UserDto;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(value = {SpringExtension.class})
public class JwtsTest {
    @Spy
    ObjectMapper objectMapper;
    @InjectMocks
    TokenProvider tokenProvider;

    @Test
    void decrypt() {
        String token = "eyJ0b2tlblR5cGUiOiJBQ0NFU1MiLCJhbGciOiJIUzI1NiJ9.eyJCT0RZIjoie1widXVpZFwiOlwiNjM0NmNmNDI5MTkwYzFkMGM3NTMyZGE2XCIsXCJpZFwiOlwiMDEwMjcxMTQxNjBcIixcImxhc3RNb2RpZmllZERhdGVcIjpcIjIwMjQtMTAtMTJUMTQ6NTU6MDAuMTg4XCIsXCJqb2luRGF0ZVwiOlwiMjAyNC0xMC0xMlQxNDo1NTowMC4xODhcIn0iLCJpc3MiOiJmaXRCdWRkeSIsImp0aSI6IjYzNDZjZjQyOTE5MGMxZDBjNzUzMmRhNiIsImV4cCI6MTcyOTMxNzc3MiwibmJmIjoxNzI4NzEyOTcyLCJpYXQiOjE3Mjg3MTI5NzJ9.avoL5zfmQShffvOvFvuDSDjPC0LN4jMBktiKqvC6yjs";
        String SECRET_KEY = "FITBUDDYPROJECTSIGNKEYFITBUDDYPROJECTSIGNKEYFITBUDDYPROJECTSIGNKEY";

        Jwt result =  Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(token);
        System.out.println(tokenProvider.decrypt(token, UserDto.class));


    }
}
