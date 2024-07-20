package com.fitbuddy.service.config.security.jwt;

import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface JwtEncryptable extends UserDetails {
    String getUuid();
    String getId();
    LocalDateTime getJoinDate();
    LocalDateTime getLastModifiedDate();
}
