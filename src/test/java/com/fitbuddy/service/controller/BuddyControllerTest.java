package com.fitbuddy.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitbuddy.service.config.RestDocument;
import com.fitbuddy.service.config.enumerations.Buddy;
import com.fitbuddy.service.etc.uuid.Uuid;
import com.fitbuddy.service.repository.dto.MyBuddyDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.fitbuddy.service.config.RestDocument.*;
import static com.fitbuddy.service.config.enumerations.Buddy.DUCK;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@SpringBootTest
@TestPropertySource(value = {"classpath:application.yaml"})
@ActiveProfiles(value = {"junit"})
@AutoConfigureMockMvc
@WebAppConfiguration
@ExtendWith(RestDocumentationExtension.class)
public class BuddyControllerTest {
    private final String prefix = "/v1/buddy";
    private  static String userUuId;
    private  static Map<String,String> header;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    public static void setUpUserUuid () {
        userUuId = Uuid.generate();
        header = Map.of(HttpHeaders.AUTHORIZATION, "");
    }

    @Nested
    @DisplayName(value = "친구 만들기")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class MakeAFriend {
        MyBuddyDto buddyDto;
        Buddy buddy = DUCK;
        String name = "도널드";

        private final String url = prefix+"/make-friends";

        @BeforeEach
        void setUp() {

            buddyDto = new MyBuddyDto();
            buddyDto.setUserUuid(userUuId);
            buddyDto.setBuddy(buddy);
            buddyDto.setName(name);
        }

        @Test
        @DisplayName("실패 - 사용자 UUID 누락")
        @Order(1)
        void failure_userUuidIsEmpty() throws Exception {
            buddyDto.setUserUuid(null);

            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform (
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$").isString())
            .andDo(print());


        }

        @Test
        @DisplayName("실패 - Buddy 누락")
        @Order(2)
        void failure_buddyIsEmpty() throws Exception {
            buddyDto.setBuddy(null);
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform (
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$").isString())
            .andDo(print());

        }

        @Test
        @DisplayName("실패 - 이름 누락")
        @Order(3)
        void failure_nameIsEmpty() throws Exception {
            buddyDto.setName("");
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform (
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$").isString())
            .andDo(print());

        }

        @Test
        @DisplayName("실패 - 이미 있는 Buddy")
        @Order(5)
        void failure_duplicatedBuddy() throws Exception {
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform (
                            post(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .header(HttpHeaders.AUTHORIZATION, "accessToken")
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$" ).value("이미 친구가 된 버디입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("성공")
        @Order(4)
        void success() throws Exception {

            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform (
                            post(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .header(HttpHeaders.AUTHORIZATION, "accessToken")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").isString())
                    .andExpect(jsonPath("$.userUuid").value(userUuId))
                    .andExpect(jsonPath("$.buddy").value(DUCK.name()))
                    .andExpect(jsonPath("$.isPrimary").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.name").value(name))
                    .andExpect(jsonPath("$.exp").value(0L))
                    .andDo(print())
                    .andDo(
                        RestDocument.build("친구 사귀기")
                                    .headersSnippet(simpleHeaders(
                                            Map.of(HttpHeaders.AUTHORIZATION, "액세스 토큰")
                                    ))
                                    .rqSnippet(simpleRequestFields(
                                            Map.of(
                                                    "userUuid", "사용자 고유번호",
                                                    "buddy", "버디 종류",
                                                    "name", "이름"
                                            )
                                    ))
                                    .rsSnippet(simpleResponseFields(
                                            Map.of(
                                                    "uuid","버디 UUID",
                                                    "userUuid","사용자 UUID",
                                                    "buddy","버디 종류",
                                                    "isPrimary","현재 선택 여부",
                                                    "name","이름",
                                                    "exp","경험치",
                                                    "action","현재 액션",
                                                    "whenStart","액션 시작일",
                                                    "whenEnd","액션 종료 예정일",
                                                    "actionHistories","액션 히스토리"
                                            )
                                    )).build()
                    );
        }


    }

    @Nested
    @DisplayName(value = "내 친구들")
    class MyBuddies {
        private final String url = prefix+"/{userUuid}";
        private  static String userUuId  = "3a4dfec64f885529ac4ff0d2";


        @Test
        @DisplayName("성공")
        void success() throws Exception {

            mockMvc.perform (
                            get(url, userUuId).contentType(MediaType.APPLICATION_JSON)
                                    .header(HttpHeaders.AUTHORIZATION, "accessToken")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[*].uuid").exists())
                    .andExpect(jsonPath("$[?(@.userUuid == '%s')]", this.userUuId).exists())
                    .andExpect(jsonPath("$[*].buddy").exists())
                    .andExpect(jsonPath("$[*].uuid").exists())
                    .andExpect(jsonPath("$[*].name").exists())
                    .andDo(print())
                    .andDo(
                            RestDocument.build("내 친구들")
                                    .headersSnippet(simpleHeaders(
                                            Map.of(HttpHeaders.AUTHORIZATION, "액세스 토큰")
                                    ))
                                    .pathSnippet(simplePathParameters(Map.of("userUuid", "사용자 UUID")))
                                    .rsSnippet(simpleResponseFields(
                                            Map.of(
                                                    "[].uuid","버디 UUID",
                                                    "[].userUuid","사용자 UUID",
                                                    "[].buddy","버디 종류",
                                                    "[].isPrimary","현재 선택 여부",
                                                    "[].name","이름",
                                                    "[].exp","경험치",
                                                    "[].action","현재 액션",
                                                    "[].whenStart","액션 시작일",
                                                    "[].whenEnd","액션 종료 예정일",
                                                    "[].actionHistories","액션 히스토리"
                                            )
                                    )).build()
                    );
        }


    }

    @Nested
    @DisplayName(value = "경험치 획득")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class EarnExp {
        MyBuddyDto buddyDto;
        private String uuid = "e72cbaa4b1a4ed562486bae3";
        private final String url = prefix+"/earn-exp";

        @BeforeAll
        void setBuddyDto () {
            buddyDto = new MyBuddyDto();
            buddyDto.setUuid(uuid);
            buddyDto.setExp(0L);
        }

        @Test
        @DisplayName(value = "실패 uuid 누락")
        void failure_uuidIsEmpty() throws Exception {
            buddyDto.setUuid(null);

            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                    patch(url).contentType(MediaType.APPLICATION_JSON)
                              .content(json)
            )
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$").isString())
            .andDo(print());
        }

        @Test
        @DisplayName(value = "실패 exp 누락")
        void failure_expIsEmpty() throws Exception {
            buddyDto.setExp(null);

            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                            patch(url).contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "실패 exp 최소 값 불만족")
        void failure_expMinNotValidated() throws Exception {
            buddyDto.setExp(-1L);

            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                            patch(url).contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "성공")
        void success() throws Exception {
            buddyDto.setExp(100L);
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                            patch(url)
                                    .header(HttpHeaders.AUTHORIZATION, "ACCESS TOKEN")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(Boolean.TRUE))
                    .andDo(print())
                    .andDo(
                        RestDocument.build("경험치 조정")
                                .headersSnippet(simpleHeaders(
                                        Map.of(HttpHeaders.AUTHORIZATION, "액세스 토큰")
                                ))
                                .rqSnippet(simpleRequestFields(
                                    Map.of(
                                            "uuid", "버디 고유 값",
                                            "exp", "경험치 (0 ~ 100)"
                                    )
                                ))
                                .build()
                    );
        }


    }


    @Nested
    @DisplayName(value = "메인 버디 설정")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ChangePrimaryBuddy{
        private final String url = prefix+"/see-ya";
        MyBuddyDto buddyDto;
        @BeforeAll
        void setMyBuddyDto() {
            buddyDto = new MyBuddyDto();
            buddyDto.setUuid("e72cbaa4b1a4ed562486bae3");
            buddyDto.setUserUuid("3a4dfec64f885529ac4ff0d2");
        }


        @Test
        @DisplayName(value = "실패 - 사용자 Uuid 누락")
        void failure_userUuidIsEmpty ( ) throws Exception {
            buddyDto.setUserUuid(null);
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                            patch(url).contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .header(HttpHeaders.AUTHORIZATION, "AccessToken")
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andDo(print());
        }


        @Test
        @DisplayName(value = "실패 - 버디 Uuid 누락")
        void failure_uuidIsEmpty ( ) throws Exception {
            buddyDto.setUuid(null);
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                            patch(url).contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .header(HttpHeaders.AUTHORIZATION, "AccessToken")
                    )
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$").isString())
                    .andDo(print());
        }

        @Test
        @DisplayName(value = "성공")
        void success ( ) throws Exception {
            String json = objectMapper.writeValueAsString(buddyDto);

            mockMvc.perform(
                            patch(url).contentType(MediaType.APPLICATION_JSON)
                                    .content(json)
                                    .header(HttpHeaders.AUTHORIZATION, "AccessToken")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isBoolean())
                    .andDo(print())
                    .andDo(
                            RestDocument.build("메인 버디 설정")
                                    .rqSnippet(simpleRequestFields(
                                            Map.of("uuid", "버디 UUID")
                                    ))
                                    .headersSnippet(simpleHeaders(
                                            Map.of(HttpHeaders.AUTHORIZATION, "액세스 토큰")
                                    ))
                                    .build()
                    );
        }
    }


    @Nested
    @DisplayName(value = "도감")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Dictionary {
        private String url = prefix+"/{userUuid}/dictionary";
        private String userUuid = "3a4dfec64f885529ac4ff0d2";

        @Test
        @DisplayName("성공")
        void success () throws Exception {
            mockMvc.perform(
                get(url, userUuid)
                .header(HttpHeaders.AUTHORIZATION, "AccessToken")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[*]", containsInAnyOrder("CHICKEN", "DUCK")))
            .andDo(print())
            .andDo(RestDocument.build("버디 도감")
                    .pathSnippet(simplePathParameters(Map.of("userUuid", "사용자 Uuid")))
                    .headersSnippet(simpleHeaders(Map.of(HttpHeaders.AUTHORIZATION, "액세스 토큰")))
                    .rsSnippet(simpleResponseFields( Map.of("[]", "버디 종류")))
                    .build()
            );
        }


    }
//    "uuid":"e72cbaa4b1a4ed562486bae3","userUuid":"3a4dfec64f885529ac4ff0d2","buddy":"DUCK","isPrimary":true,"name":"도널드","exp":0,
}
