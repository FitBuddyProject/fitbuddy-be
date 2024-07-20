package com.fitbuddy.service.repository.entity;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Collation(value = "UTF-8")
@Document(collection = "User")
@Getter
public class User {
    @Id
    @Field(name = "uuid", targetType = FieldType.OBJECT_ID)
    private String uuid;

    @Field(name = "phone", targetType = FieldType.STRING)
    private String phone;
    @Field(name = "password", targetType = FieldType.STRING)
    private String password;
    @Field(name = "nickname", targetType = FieldType.STRING)
    private String nickname;
    @Field(name = "email", targetType = FieldType.STRING)
    private String email;
    @Field(name = "refreshToken", targetType = FieldType.STRING)
    private String refreshToken;
    @Field(name = "pushToken", targetType = FieldType.STRING)
    private String pushToken;
    @Field(name = "tired", targetType = FieldType.INT64)
    private Long tired;
    @Field(name = "sendable", targetType = FieldType.BOOLEAN)
    private Boolean sendable;

    @CreatedDate@Field(name = "joinDate", targetType = FieldType.DATE_TIME)
    private LocalDateTime joinDate;
    @LastModifiedDate@Field(name = "lastModifiedDate", targetType = FieldType.DATE_TIME)
    private LocalDateTime lastModifiedDate;
    @Field(name = "lastSignInDate", targetType = FieldType.DATE_TIME)
    private LocalDateTime lastSignInDate;

    @DBRef(lazy = true)
    private List<MyBuddy> buddies;
}
