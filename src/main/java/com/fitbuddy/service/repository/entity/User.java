package com.fitbuddy.service.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.uuid.Generators;
import com.fitbuddy.service.config.security.jwt.JwtEncryptable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@ToString
@Collation(value = "UTF-8")
@Document(collection = "User")
@Getter
@Setter
@JsonIgnoreProperties(value = {
        "password",
        "authorities",
        "username",
        "accountNonExpired",
        "accountNonLocked",
        "credentialsNonExpired",
        "enabled",
        "refreshToken",
        "isNew",
        "getId",
        "isNew"
}, allowGetters = false, allowSetters = true)

public class User implements Persistable {
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String uuid;

    @Field(name = "phone", targetType = FieldType.STRING)
    @Indexed(name = "name_idx", background = true, sparse = false)
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
    @Indexed(name = "sendable_idx", background = true, sparse = true)
    private Boolean sendable;

    @CreatedDate@Field(name = "joinDate", targetType = FieldType.DATE_TIME)
    private LocalDateTime joinDate;
    @LastModifiedDate@Field(name = "lastModifiedDate", targetType = FieldType.DATE_TIME)
    private LocalDateTime lastModifiedDate;
    @Field(name = "lastSignInDate", targetType = FieldType.DATE_TIME)
    private LocalDateTime lastSignInDate;

    @DBRef
    private List<MyBuddy> buddies;

    @Transient
    private Boolean isNew = Boolean.FALSE;



    @Override
    public Object getId() {
        return this.uuid;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
