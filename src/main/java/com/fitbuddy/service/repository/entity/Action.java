package com.fitbuddy.service.repository.entity;

import com.fitbuddy.service.config.enumerations.Act;
import com.fitbuddy.service.config.enumerations.ActionStatus;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "ActionHistory")
@Getter
public class Action implements Persistable {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String uuid;
    @Field(name = "User_id")
    private String userUuid;
    @Field(name = "MyBuddy_id")
    private String myBuddyUuid;
    @Field(name = "action")
    @Indexed(sparse = true, name = "action_index")
    private Act action;
    @Field(name = "actionStatus")
    private ActionStatus actionStatus;
    @Field(name = "start", targetType = FieldType.DATE_TIME)
    private LocalDateTime start;
    @Field(name = "end", targetType = FieldType.DATE_TIME)
    private LocalDateTime end;
    @Field(name = "athlete")
    private Athlete athlete;

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
