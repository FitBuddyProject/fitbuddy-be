package com.fitbuddy.service.repository.entity;

import com.fitbuddy.service.config.enumerations.Action;
import com.fitbuddy.service.config.enumerations.ActionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "ActionHistory")
public class ActionHistory {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String uuid;

    @Field(name = "action")
    private Action action;
    @Field(name = "actionStatus")
    private ActionStatus actionStatus;
    @Field(name = "start", targetType = FieldType.DATE_TIME)
    private LocalDateTime start;
    @Field(name = "end", targetType = FieldType.DATE_TIME)
    private LocalDateTime end;
}
