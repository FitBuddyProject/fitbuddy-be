package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Intensity;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Data
public class AthleteDto {
    private String uuid;
    private String userUuid;
    private String exerciseType;
    private Intensity intensity;
    private int duration;
    private String diary;
    private LocalDate date;

    private Boolean isNew = Boolean.FALSE;

    public AthleteDto beforeInsert (String uuid) {
        if(!StringUtils.hasText(this.uuid)){
            this.uuid = uuid;
            isNew = Boolean.TRUE;
        }
        return this;
    }
}
