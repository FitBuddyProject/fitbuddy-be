package com.fitbuddy.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitbuddy.service.etc.enumerates.Header;
import com.fitbuddy.service.repository.dto.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.AntPathMatcher;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(value = {"classpath:application.yaml"})
@ActiveProfiles(value = {"junit"})
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final String prefix = "/v1/user";


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class SignUpTest {
        private final String url = "/sign/up";
        private UserDto userDto;
        @BeforeAll
        public void setup () {
            userDto = new UserDto();
            userDto.setPhone("01011111117");
            userDto.setPassword("1212");
            userDto.setNickname("test");
        }

        @Test
        public void success () throws Exception {

            String bodyJson = objectMapper.writeValueAsString(userDto);
            mockMvc.perform(
                         post(prefix+url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson)
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().exists(Header.ACCESS_TOKEN.getValue()))
                    .andExpect(header().exists(Header.REFRESH_TOKEN.getValue()))
                    .andExpect(jsonPath("$.uuid").isString())
                    .andExpect(jsonPath("$.password").doesNotExist())
                    .andDo(print());

        }
    }
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class SignInTest {
        private UserDto userDto;
        private final String url = "/sign/in";
        @BeforeAll
        public void setup () {
            userDto = new UserDto();
            userDto.setPhone("01011111111");
            userDto.setPassword("1212");
        }

        @Test
        public void success () throws Exception {

            String bodyJson = objectMapper.writeValueAsString(userDto);
            mockMvc.perform(
                            patch(prefix+url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(bodyJson)
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().exists(Header.ACCESS_TOKEN.getValue()))
                    .andExpect(header().exists(Header.REFRESH_TOKEN.getValue()))
                    .andExpect(jsonPath("$.uuid").isString())
                    .andExpect(jsonPath("$.password").doesNotExist())
                    .andDo(print());

        }
    }
}
