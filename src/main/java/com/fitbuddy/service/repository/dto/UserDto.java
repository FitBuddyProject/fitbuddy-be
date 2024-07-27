package com.fitbuddy.service.repository.dto;

import com.fasterxml.uuid.Generators;
import com.fitbuddy.service.config.security.jwt.JwtEncryptable;
import com.fitbuddy.service.config.security.jwt.TokenProvider;
import com.fitbuddy.service.repository.entity.MyBuddy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"uuid", "phone"})
public class UserDto implements JwtEncryptable{
    private String uuid;
    private String phone;
    private String password;
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
    @Setter(AccessLevel.PRIVATE)
    private Boolean isNew = Boolean.FALSE;
    public UserDto beforeInsert (BCryptPasswordEncoder encoder, String refreshToken) {
        this.password = encoder.encode(this.password);
        this.uuid = Generators.timeBasedGenerator().generate().toString();

        this.joinDate = LocalDateTime.now();
        this.lastModifiedDate = this.joinDate;
        this.isNew = Boolean.TRUE;
        this.sendable = Boolean.FALSE;

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
    public String getId() {
        return this.phone;
    }

}
