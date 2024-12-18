package com.fitbuddy.service.service;

import com.fitbuddy.service.config.security.jwt.TokenProvider;
import com.fitbuddy.service.etc.enumerates.Header;
import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.repository.entity.User;
import com.fitbuddy.service.repository.user.UserRepository;
import com.fitbuddy.service.repository.user.UserTemplate;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
//@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final BCryptPasswordEncoder bcrypt;
    private final UserRepository repository;
    private final UserTemplate template;
    private final ModelMapper mapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;
    private final Environment environment;


    private String generateCode() {
        return IntStream
                .generate(() -> new SecureRandom().nextInt(10))
                .limit(6)
                .boxed()
                .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append).toString();
    }

    public String verify(String phone) {

        String random = this.generateCode();
        ValueOperations<String, String> value = redisTemplate.opsForValue();

        boolean isDev = environment.getProperty("spring.config.activate.profile", Boolean.class);
        if (StringUtils.hasText(value.get(phone)) && !isDev) throw new IllegalStateException("이미 발송됐습니다.");
        value.set(phone, random, Duration.ofMinutes(5L));

        return random;
    }

    public User signUp(HttpServletResponse response, UserDto dto) {
        if( template.isDuplicated(dto.getPhone()) ) throw new IllegalStateException("이미 존재하는 아이디입니다.");



        dto.beforeGenerateRefresh();
//        dto.setPassword(bcrypt.encode(dto.getPassword()));
        String refresh = tokenProvider.encrypt(dto, Boolean.FALSE);
        String access = tokenProvider.encrypt(dto, Boolean.TRUE);
        User user = mapper.map(dto.beforeInsert(bcrypt, refresh), User.class);

        response.addHeader(Header.ACCESS_TOKEN.getValue(), access);
        response.addHeader(Header.REFRESH_TOKEN.getValue(), refresh);

        User result = repository.save(user);;
        return result;
    }

    public User signIn(HttpServletResponse response, UserDto userDto) {

        Optional<User> find = repository.findUserByPhone(userDto.getPhone());
        User user = find.orElseThrow(() -> new IllegalArgumentException("계정을 확인해주세요."));

        log.error("USER{}", user.getBuddies());
//        if( !bcrypt.matches( userDto.getPassword(), user.getPassword() ) ) throw new IllegalArgumentException("아이디 혹은 비밀번호를 확인해주세요.");
        UserDto dto = mapper.map(user, UserDto.class);
        String refresh = tokenProvider.encrypt(dto, Boolean.FALSE);
        String access = tokenProvider.encrypt(dto, Boolean.TRUE);

        if(StringUtils.hasText(dto.getPushToken())) {
            template.syncUser(userDto.getUuid(),refresh, userDto.getPushToken());
        }

        response.addHeader(Header.ACCESS_TOKEN.getValue(), access);
        response.addHeader(Header.REFRESH_TOKEN.getValue(), refresh);

        return user;
    }

    public Boolean signOut(HttpServletResponse response, UserDto userDto) {
        response.addHeader(Header.ACCESS_TOKEN.getValue(), "");
        response.addHeader(Header.REFRESH_TOKEN.getValue(), "");
        return template.signOut(userDto);
    }

    public Boolean syncPushToken(UserDto user) {
        return template.syncPushToken(user);
    }

    public Boolean syncTired(UserDto user) {return template.syncTired(user);
    }

    public Boolean sendable(UserDto user) {return template.sendable(user);}
}
