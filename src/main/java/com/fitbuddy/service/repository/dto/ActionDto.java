package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Act;
import com.fitbuddy.service.config.enumerations.ActionStatus;
import com.fitbuddy.service.repository.entity.Athlete;
import lombok.Data;

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

}
