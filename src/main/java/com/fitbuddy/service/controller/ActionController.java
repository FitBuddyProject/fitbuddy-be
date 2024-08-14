package com.fitbuddy.service.controller;

import com.fitbuddy.service.repository.dto.AthleteDto;
import com.fitbuddy.service.repository.dto.request.ActionRequest;
import com.fitbuddy.service.service.ActionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/action")
@RequiredArgsConstructor
@Slf4j
public class ActionController {
    private final ActionService service;

    public ResponseEntity calendar(ActionRequest request) {
        return null;
    }



    @GetMapping(value = "/{uuid}")
    public ResponseEntity detail(@PathVariable(name = "uuid") String uuid) {
        return null;
    }

    public ResponseEntity doAction(@RequestBody AthleteDto ahAthlete) {
        return null;
    }

    public ResponseEntity cancelAction(@RequestBody AthleteDto ahAthlete) {
        return null;
    }

    public ResponseEntity histories(ActionRequest request) {
        return null;
    }
}
