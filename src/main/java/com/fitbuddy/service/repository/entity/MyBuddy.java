package com.fitbuddy.service.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fitbuddy.service.config.enumerations.Action;
import com.fitbuddy.service.config.enumerations.Buddy;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "MyBuddy")
@Getter
@ToString
@JsonIgnoreProperties(value = {"new", "id"})
public class MyBuddy  implements Persistable {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String uuid;
    @Field(name = "user_id")
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

    @Transient
    private boolean isNew;

    @DBRef
    private List<ActionHistory> actionHistories;

    @Override
    public Object getId() {
        return this.uuid;
    }
    @Override
    public boolean isNew() {
        return this.isNew;
    }

}
