package com.fitbuddy.service.repository.entity;

import com.fitbuddy.service.config.enumerations.Action;
import com.fitbuddy.service.config.enumerations.Buddy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "MyBuddy")
public class MyBuddy {

    @Id
    @Field(name = "uuid", targetType = FieldType.OBJECT_ID)
    private String uuid;
    @Field(name = "userUuid")
    private String userUuid;
    @Field(name = "buddy")
    private Buddy buddy;
    @Field(name = "isPrimary", targetType = FieldType.BOOLEAN)
    private Boolean isPrimary;
    @Field(name = "name", targetType = FieldType.SCRIPT)
    private String name;
    @Field(name = "exp", targetType = FieldType.INT64)
    private Long exp;


    @Field(name = "action")
    private Action action;
    @Field(name = "whenStart", targetType = FieldType.DATE_TIME)
    private LocalDateTime whenStart;
    @Field(name = "whenEnd", targetType = FieldType.DATE_TIME)
    private LocalDateTime whenEnd;

    @DBRef
    private List<ActionHistory> actionHistories;
}
