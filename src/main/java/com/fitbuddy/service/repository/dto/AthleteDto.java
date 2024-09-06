package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Intensity;
import com.fitbuddy.service.etc.uuid.Uuid;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

@Data
public class AthleteDto {
    private String uuid;
    private String userUuid;
    private String exerciseType;
    private Intensity intensity;
    private int duration;
    private String diary;

    private Boolean isNew = Boolean.FALSE;

    public AthleteDto beforeInsert (String uuid) {
        if(!StringUtils.hasText(this.uuid)){
            this.uuid = uuid;
            isNew = Boolean.TRUE;
        }
        return this;
    }
}
