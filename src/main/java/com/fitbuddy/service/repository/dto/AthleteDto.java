package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Intensity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class AthleteDto {
    private String exerciseType;
    private Intensity intensity;
    private int duration;
    private String diary;
}
