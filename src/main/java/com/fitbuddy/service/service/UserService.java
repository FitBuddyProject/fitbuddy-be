package com.fitbuddy.service.service;

import com.fitbuddy.service.config.security.jwt.TokenProvider;
import com.fitbuddy.service.enumerations.Header;
import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.repository.entity.User;
import com.fitbuddy.service.repository.user.UserRepository;
import com.fitbuddy.service.repository.user.UserTemplate;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder bcrypt;
    private final UserRepository repository;
    private final UserTemplate template;
    private final ModelMapper mapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;


    private String generateCode() {
        return IntStream
                .generate(() -> new SecureRandom().nextInt(10))
                .limit(8)
                .boxed()
                .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append).toString();
    }

    public String verify(String phone) {

        String random = this.generateCode();
        ValueOperations<String, String> value = redisTemplate.opsForValue();

        if (StringUtils.hasText(value.get(phone))) throw new IllegalStateException("이미 발송됐습니다.");
        value.set(phone, random, Duration.ofMinutes(5L));

        return random;
    }

    public User signUp(HttpServletResponse response, UserDto dto) {
        if( template.isDuplicated(dto.getPhone()) ) throw new IllegalStateException("이미 존재하는 아이디입니다.");

        String refresh = tokenProvider.encrypt(dto, Boolean.FALSE);
        String access = tokenProvider.encrypt(dto, Boolean.TRUE);
        User user = mapper.map(dto.beforeInsert(bcrypt, refresh), User.class);

        response.addHeader(Header.ACCESS_TOKEN.getValue(), access);
        response.addHeader(Header.REFRESH_TOKEN.getValue(), refresh);

        return repository.save(user);
    }

    public User signIn(HttpServletResponse response, UserDto userDto) {

        Optional<User> find = repository.findUserByPhone(userDto.getPhone());
        User user = find.orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호를 확인해주세요."));

        if( !bcrypt.matches( userDto.getPassword(), user.getPassword() ) ) throw new IllegalArgumentException("아이디 혹은 비밀번호를 확인해주세요.");

        UserDto dto = mapper.map(user, UserDto.class);
        String refresh = tokenProvider.encrypt(dto, Boolean.FALSE);
        String access = tokenProvider.encrypt(dto, Boolean.TRUE);

        template.updatePushToken(userDto.getUuid(),refresh, userDto.getPushToken());
        response.addHeader(Header.ACCESS_TOKEN.getValue(), access);
        response.addHeader(Header.REFRESH_TOKEN.getValue(), refresh);

        return user;
    }

    public Boolean signOut(HttpServletResponse response, UserDto userDto) {
        response.addHeader(Header.ACCESS_TOKEN.getValue(), "");
        response.addHeader(Header.REFRESH_TOKEN.getValue(), "");
        return template.signOut(userDto);
    }
}
