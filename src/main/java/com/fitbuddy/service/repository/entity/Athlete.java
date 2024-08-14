package com.fitbuddy.service.repository.entity;

import com.fitbuddy.service.config.enumerations.Intensity;
import org.springframework.data.mongodb.core.mapping.Field;


public class Athlete {
    @Field(name = "exerciseType")
    private String exerciseType;
    @Field(name = "intensity")
    private Intensity intensity;
    @Field(name = "duration")
    private int duration;
    @Field(name = "diary")
    private String diary;
}
