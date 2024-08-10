package com.fitbuddy.service.service;

import com.fitbuddy.service.config.security.jwt.TokenProvider;
import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.repository.entity.User;
import com.fitbuddy.service.repository.user.UserRepository;
import com.fitbuddy.service.repository.user.UserTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(value = {SpringExtension.class})
@Profile(value = "junit")
public class UserServiceTest {

    @Mock
    private TokenProvider tokenProvider;
    @Spy
    private BCryptPasswordEncoder bcrypt;
    @Mock
    private UserRepository repository;
    @Mock
    private UserTemplate template;
    @Spy
    private ModelMapper mapper;
    @Mock(answer = Answers.RETURNS_MOCKS)
    private MongoTemplate mongoTemplate;
    @Spy
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @InjectMocks
    private UserService service;

    @Nested
    @DisplayName("인증번호 발급 테스트")
    class VerifyTest {
        @Test
        @DisplayName(value = "인증번호 발급 및 부여")
        public void success () {
            doReturn(valueOperations).when(redisTemplate).opsForValue();
            assertThat(service.verify("").length()).isEqualTo(8);
        }

        @Test
        @DisplayName(value = "인증번호 기발급 및 에러")
        public void failure () {
            String phone = "01012341234";
            doReturn(valueOperations).when(redisTemplate).opsForValue();
            doReturn("00000000").when(valueOperations).get(phone);

            assertThatThrownBy(() -> service.verify(phone)).isInstanceOf(IllegalStateException.class);
        }
    }


    @Nested
    @DisplayName("회원 가입")
    class SignUpTest {
        @Test
        @DisplayName("- 성공")
        public void success () {
            String phone = "01012341234";
            String password = "password";
            String nickname = "fitBuddy";
            String email = "fitbuddy@gmail.com";

            UserDto userDto = new UserDto();
            userDto.setPhone(phone);
            userDto.setPassword(password);
            userDto.setNickname(nickname);
            userDto.setEmail(email);
            userDto.beforeGenerateRefresh();
            userDto.beforeInsert(bcrypt, "");
            HttpServletResponse response = new MockHttpServletResponse();

            User user = mapper.map(userDto, User.class);


            doReturn(Boolean.FALSE).when(template).isDuplicated(phone);
            doReturn("").when(tokenProvider).encrypt(userDto, true);
            doReturn("").when(tokenProvider).encrypt(userDto, false);
            when(repository.save(any(User.class))).thenReturn(user);



            assertThat(service.signUp(response, userDto))
                    .isExactlyInstanceOf(User.class)
                    .extracting(
                            "phone",
                            "nickname",
                            "email",
                            "isNew",
                            "sendable"
                    )
                    .isEqualTo(
                            tuple(
                                    phone,
                                    nickname,
                                    email,
                                    Boolean.TRUE,
                                    Boolean.FALSE
                            ).toList()
                    );
        }

        @Test
        @DisplayName("- 실패 : 이미 가입된 휴대폰 번호")
        public void duplicated () {
            String phone = "01012341234";
            String password = "password";
            String nickname = "fitBuddy";
            String email = "fitbuddy@gmail.com";

            UserDto userDto = new UserDto();
            userDto.setPhone(phone);
            userDto.setPassword(password);
            userDto.setNickname(nickname);
            userDto.setEmail(email);
            HttpServletResponse response = new MockHttpServletResponse();

            doReturn(Boolean.TRUE).when(template).isDuplicated(phone);


            assertThatThrownBy(() -> service.signUp(response, userDto)).isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("회원 로그인")
    class SignInTest {


        @Test
        @DisplayName("- 실패 : 아이디 X")
        void failureWrongPhone() {
            String phone = "01000000000";
            UserDto userDto = new UserDto();
            userDto.setPhone(phone);

            doReturn(Optional.empty()).when(repository).findUserByPhone(phone);


            assertThatThrownBy(() -> service.signIn(new MockHttpServletResponse(), userDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아이디 혹은 비밀번호를 확인해주세요.");
        }

        @Test
        @DisplayName("- 실패 : 아이디 O 비밀번호 X")
        void failureWrongPassword() {
            String phone = "01012341234";
            String password = "password";
            UserDto userDto = new UserDto();
            userDto.setPhone(phone);
            userDto.setPassword(password);

            doReturn(Optional.of(mapper.map(userDto, User.class))).when(repository).findUserByPhone(phone);

            assertThatThrownBy(() -> service.signIn(new MockHttpServletResponse(), userDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아이디 혹은 비밀번호를 확인해주세요.");
        }


        @Test
        @DisplayName("- 성공")
        void success() {
            String uuid = "UUID";
            String phone = "01012341234";
            String password = "password";
            String passwordEnc = bcrypt.encode(password);
            String access = "ACCESS";
            String refresh = "REFRESH";
            UserDto userDto = new UserDto();
            userDto.setPhone(phone);
            userDto.setPassword(password);

            User user = mapper.map(userDto, User.class);
            user.setUuid(uuid);
            user.setPassword(passwordEnc);


            doReturn(Optional.of(user)).when(repository).findUserByPhone(anyString());
            doReturn(access).when(tokenProvider).encrypt(userDto, true);
            doReturn(refresh).when(tokenProvider).encrypt(userDto, false);
            doReturn(Boolean.TRUE).when(template).syncUser(anyString(), anyString(), anyString());


            assertThat(service.signIn(new MockHttpServletResponse(), userDto))

                    .extracting("phone", "password")
                    .isEqualTo(tuple(phone, passwordEnc).toList());
        }
    }
}
