package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Act;
import com.fitbuddy.service.config.enumerations.ActionStatus;
import com.fitbuddy.service.etc.uuid.Uuid;
import com.fitbuddy.service.repository.entity.Athlete;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
public class ActionDto {

    private String uuid;
    private String userUuid;
    private String myBuddyUuid;
    private Act action;
    private ActionStatus actionStatus;
    private LocalDateTime start;
    private LocalDateTime end;
    private AthleteDto athlete;

    private Boolean isNew = Boolean.FALSE;

    public ActionDto beforeInsert () {
        if(!StringUtils.hasText(this.uuid)){
            this.uuid = Uuid.generate();
            isNew = Boolean.TRUE;
        }
        return this;
    }
}
