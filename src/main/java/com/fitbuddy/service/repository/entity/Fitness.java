package com.fitbuddy.service.repository.entity;

import com.fitbuddy.service.config.enumerations.Intensity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;

@Document(collection = "Fitness")
public class Fitness {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String uuid;
    private String userUuid;
    private String type;
    private int duration;
    private Intensity intensity;
    private String memo;
    private LocalDate when;

}
