package com.fitbuddy.service.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest()
@TestPropertySource(value = {"classpath:application.yaml"})
@ActiveProfiles(value = {"junit"})
@AutoConfigureMockMvc
public class UserControllerTest {

    @Nested
    public class SignInTest {
        @Test
        public void success () {

        }
    }
}
