package com.fitbuddy.service.service;

import com.fitbuddy.service.repository.action.ActionTemplate;
import com.fitbuddy.service.repository.dto.ActionDto;
import com.fitbuddy.service.repository.dto.AthleteDto;
import com.fitbuddy.service.repository.dto.request.ActionRequest;
import com.fitbuddy.service.repository.entity.Action;
import com.fitbuddy.service.repository.entity.Athlete;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ActionService {
    private final ActionTemplate template;
    private final ModelMapper mapper;
    public List<Athlete> calendar(ActionRequest request) {
        return template.calendar(request);
    }

    public Athlete detail(String uuid) {
        return template.detail(uuid);
    }

    public List<Action> histories(ActionRequest request) {
        return template.histories(request);
    }

    public Boolean doAction(ActionDto actionDto) {

        ActionDto beforeInsert = actionDto.beforeInsert();
        Action action = mapper.map(beforeInsert, Action.class);

        template.saveAction(action);
        if(Objects.nonNull(actionDto.getAthlete())) {
            AthleteDto athleteBeforeInsert = actionDto.getAthlete().beforeInsert(beforeInsert.getUuid());
            Athlete athlete = mapper.map(athleteBeforeInsert, Athlete.class);
            template.saveAthlete(athlete);
        }

        return Boolean.TRUE;
    }
}
