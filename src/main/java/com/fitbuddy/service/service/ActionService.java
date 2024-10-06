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


        this.startFriendStatus(actionDto);
        this.doAthlete(actionDto.getAthlete(), action.getUuid());
        return Boolean.TRUE;
    }

    private void  doAthlete(AthleteDto athleteDto, String uuid) {
        if(Objects.nonNull(athleteDto)) {
            AthleteDto athleteBeforeInsert = athleteDto.beforeInsert(uuid);
            Athlete athlete = mapper.map(athleteBeforeInsert, Athlete.class);
            template.saveAthlete(athlete);
        }
    }

    private void startFriendStatus(ActionDto actionDto) {
        template.startFriendStatus( actionDto );
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
