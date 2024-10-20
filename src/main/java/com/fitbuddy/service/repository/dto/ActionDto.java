package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Act;
import com.fitbuddy.service.config.enumerations.ActionStatus;
import com.fitbuddy.service.etc.uuid.Uuid;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

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
            this.actionStatus = ActionStatus.ON_GOING;
        }
        return this;
    }

    public ActionDto prepareAthlete() {
        if(Objects.nonNull(athlete))athlete.setDate(start.toLocalDate());
        return this;
    }
}
