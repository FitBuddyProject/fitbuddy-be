package com.fitbuddy.service.config.security.jwt;

import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface JwtEncryptable extends UserDetails {
    String getUuid();
    void decodeUUID();
    void encodeUUID();
    String getId();
    LocalDateTime getJoinDate();
    LocalDateTime getLastModifiedDate();
}
