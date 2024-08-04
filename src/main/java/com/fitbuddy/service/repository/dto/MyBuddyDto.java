package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Action;
import com.fitbuddy.service.config.enumerations.Buddy;
import com.fitbuddy.service.repository.entity.ActionHistory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MyBuddyDto {

    private String uuid;
    private String userUuid;
    private Buddy buddy;
    private Boolean isPrimary = true;
    private String name;
    private Long exp;


    private Action action;
    private LocalDateTime whenStart;
    private LocalDateTime whenEnd;

}
