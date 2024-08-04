package com.fitbuddy.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitbuddy.service.etc.enumerates.Header;
import com.fitbuddy.service.repository.dto.UserDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@AutoConfigureRestDocs
@SpringBootTest
@TestPropertySource(value = {"classpath:application.yaml"})
@ActiveProfiles(value = {"junit"})
@AutoConfigureMockMvc
@WebAppConfiguration
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    protected RestDocumentationResultHandler restDocs;
    private final String prefix = "/v1/user";

    public static String randomPhone () {
        String mid = IntStream.rangeClosed(1, 4)
                .map(i -> new SecureRandom().nextInt(9)).boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(""));
        String fin = IntStream.rangeClosed(1, 4)
                .map(i -> new SecureRandom().nextInt(9)).boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        return String.format("010%s%s", mid, fin);
    }


    @BeforeEach
    void setUp (final WebApplicationContext context, final RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }


    @Nested
    @DisplayName(value = "인증")
    public class VerifyTest {
        private static String url = "/verify/";
        @Test
        @DisplayName(value = "성공")
        public void success () throws Exception {
            String phone = randomPhone();
            mockMvc.perform(get(String.format(prefix+url+"%s", phone)))
                   .andExpect(status().isOk());
//                   .andExpect(jsonPath("$.length()").value(8));
        }

    }


    @Nested
    @DisplayName(value = "가입 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class SignUpTest {
        private final String url = "/sign/up";
        private UserDto userDto;
        @BeforeAll
        public void setup () {
            userDto = new UserDto();
            userDto.setPhone(randomPhone());
            userDto.setPassword("1212");
            userDto.setNickname("test");
        }

        @Test
        @DisplayName(value = "회원 전화 번호 미입력")
        public void failurePhoneEmpty() throws Exception {
            userDto = new UserDto();
            userDto.setPassword("1212");
            userDto.setNickname("test");
            String bodyJson = objectMapper.writeValueAsString(userDto);
            mockMvc.perform(
                            post(prefix+url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(bodyJson)
                    )
                    .andExpect(status().is5xxServerError())
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "회원 아이디 중복")
        public void failure() throws Exception {
            userDto.setEmail("01011111111");
            String bodyJson = objectMapper.writeValueAsString(userDto);
            mockMvc.perform(
                            post(prefix+url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(bodyJson)
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("이미 존재하는 아이디입니다."));
        }

        @Test
        @DisplayName(value = "계정 생성 성공")
        @Rollback
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
    @DisplayName(value = "로그인 테스트")
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
        @DisplayName(value = "로그인 성공")
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

        @Test
        @DisplayName(value = "로그인 실패 - 아이디 틀림")
        public void failure_ID () throws Exception {
            userDto.setPhone("01000000000");
            String bodyJson = objectMapper.writeValueAsString(userDto);
            mockMvc.perform(
                            patch(prefix+url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(bodyJson)
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("아이디 혹은 비밀번호를 확인해주세요."))
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "로그인 실패 - 비밀번호 틀림")
        public void failure_PWD () throws Exception {
            userDto.setPassword("1111");
            String bodyJson = objectMapper.writeValueAsString(userDto);
            mockMvc.perform(
                            patch(prefix+url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(bodyJson)
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("아이디 혹은 비밀번호를 확인해주세요."))
                    .andDo(print());
        }
    }


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class SignOutTest {

    }
}
