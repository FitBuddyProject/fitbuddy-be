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

import java.util.List;
import java.util.Objects;

@Service
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


    public String  doAction(ActionDto actionDto) {

        ActionDto beforeInsert = actionDto.beforeInsert();
        Action action = mapper.map(beforeInsert, Action.class);

        template.saveAction(action);
        if(Objects.nonNull(actionDto.getAthlete())) {
            this.doAthlete(actionDto.prepareAthlete().getAthlete(), action.getUuid());
        }

        log.error("beforeInsert {}", beforeInsert);
        this.startFriendStatus(action);
        return beforeInsert.getUuid();
    }

    private void  doAthlete(AthleteDto athleteDto, String uuid) {
        if(Objects.nonNull(athleteDto)) {
            AthleteDto athleteBeforeInsert = athleteDto.beforeInsert(uuid);
            Athlete athlete = mapper.map(athleteBeforeInsert, Athlete.class);
            template.saveAthlete(athlete);
        }
    }

    private void startFriendStatus(Action action) {
         template.startFriendStatus( action );
    }


    public Boolean doneAction(ActionDto actionDto) {
        template.doneAction(actionDto.getUuid());
        this.doneFriendStatus(actionDto);
        return Boolean.TRUE;
    }

    private void doneFriendStatus( ActionDto actionDto ) {
        template.doneFriendStatus(actionDto);
    }

    public Boolean cancelAction(ActionDto actionDto) {
        template.cancelAction(actionDto.getUuid());
        this.doneFriendStatus(actionDto);
        return Boolean.TRUE;
    }
}
