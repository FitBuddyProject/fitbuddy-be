package com.fitbuddy.service.repository.entity;

import com.fitbuddy.service.config.enumerations.Intensity;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Document(collection = "Athlete")
@Getter
public class Athlete  implements Persistable {
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String uuid;
    @Field(name = "User_id")
    private String userUuid;
    @Field(name = "exerciseType", targetType = FieldType.STRING)
    private String exerciseType;
    @Field(name = "intensity")
    private Intensity intensity;
    @Field(name = "duration", targetType = FieldType.STRING)
    private int duration;
    @Field(name = "diary", targetType = FieldType.STRING)
    private String diary;
    @Field(name = "date", targetType = FieldType.DATE_TIME)
    @Indexed(sparse = false)
    private LocalDate date;


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
