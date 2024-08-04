package com.fitbuddy.service.repository.dto;

import com.fasterxml.uuid.Generators;
import com.fitbuddy.service.config.security.jwt.JwtEncryptable;
import com.fitbuddy.service.repository.entity.MyBuddy;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.fitbuddy.service.etc.validations.User.*;
@Data
@EqualsAndHashCode(of = {"uuid", "phone"})
public class UserDto implements JwtEncryptable {
    @NotEmpty(message = "로그아웃 시 UUID는 필수입니다.", groups = {SignOut.class})
    private String uuid;
    @NotEmpty(message = "전화번호는 필수입니다.", groups = {SignUp.class})
    private String phone;
    @NotEmpty(message = "비밀번호는 필수입니다.", groups = {SignUp.class, SignIn.class})
    private String password;
    @NotEmpty(message = "닉네임은 필수입니다.", groups = {SignUp.class})
    private String nickname;
    private String email;
    private String refreshToken;
    private String pushToken;
    private Long tired;
    @Setter(AccessLevel.PRIVATE)
    private Boolean sendable;
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime joinDate;
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime lastModifiedDate;
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime lastSignInDate;

    private List<MyBuddy> buddies;
//    @Setter(AccessLevel.NONE)
    @Transient
    private Boolean isNew = Boolean.FALSE;

    public UserDto beforeGenerateRefresh () {
        this.uuid = Generators.timeBasedGenerator().generate().toString();
        this.tired = 0L;
        this.joinDate = LocalDateTime.now();
        this.lastModifiedDate = this.joinDate;
        this.isNew = Boolean.TRUE;
        this.sendable = Boolean.FALSE;



        return this;
    }
    public UserDto beforeInsert (BCryptPasswordEncoder encoder, String refreshToken) {
        this.password = encoder.encode(this.password);
        this.refreshToken = refreshToken;

        return this;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    @Override
    public String getUsername() {
        return this.phone;
    }
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }
    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }


    @Override
    public void decodeUUID() {
        this.uuid.replaceAll("+", "-");
    }

    @Override
    public void encodeUUID() {
        this.uuid.replaceAll("-", "+");
    }

    @Override
    public String getId() {
        return this.phone;
    }

}
